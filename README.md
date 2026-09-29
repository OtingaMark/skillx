# SkillX — Skill Exchange Platform

A Kotlin Multiplatform skill-exchange app where students teach and learn from each other using a point-based economy.

## Architecture Overview

```
SkillX/
├── shared/           # Kotlin Multiplatform (domain, data, presentation, Compose UI)
├── server/           # Ktor JVM backend (REST + WebSocket, Firestore)
├── androidApp/       # Thin Android entry point
├── iosApp/           # SwiftUI wrapper for shared framework
├── functions/        # Firebase Cloud Functions (Node.js)
└── app/              # Legacy monolithic Android app (development1 only)
```

---

## Branches

### `development1` — Legacy Monolith
**Single-module Android app** (`app/`) with everything in one `MainActivity.kt` (~4,700 lines):

| Aspect | Implementation |
|--------|----------------|
| **UI** | Compose (Material 3), all screens in one file |
| **Navigation** | String-based `when(screen)` state machine |
| **Auth** | Firebase Auth (client-side) |
| **Data** | Direct Firestore reads/writes from Compose |
| **Payments** | RevenueCat IAP, client credits own points |
| **Matching** | Client pulls entire `users` collection, computes locally |
| **Lessons** | Client-side status transitions, no server validation |
| **Points** | Mutable `points` field on user doc, no ledger |
| **Security** | Client-authoritative: starting points, duplicate checks, purchase crediting, rating dedup |
| **Testing** | Minimal (template unit/instrumented tests) |
| **Modules** | `:app` only |

**Tech Stack**: Kotlin 2.2.10, AGP 9.3.2, Compose BOM 2026.02.01, Firebase Auth/Firestore, RevenueCat

---

### `development2` — Clean Multi-Module Architecture (Current)
**Full decomposition** into separate modules with clean architecture boundaries:

| Module | Responsibility |
|--------|----------------|
| **`shared`** | KMP: pure domain models, use cases, repositories, Compose Multiplatform screens, ViewModels, navigation, design system, Ktor client |
| **`server`** | Ktor JVM: REST API (`/api/v1`), WebSocket, JWT auth, Firestore data layer, JobRunr background jobs, pluggable payment providers |
| **`androidApp`** | Thin Android entry (< 100 lines): theme + `SkillXNavGraph`, Koin DI, RevenueCat init |
| **`iosApp`** | SwiftUI wrapper loading shared framework |
| **`functions`** | Unchanged Firebase Cloud Functions (M-Pesa/Daraja) |

#### Key Architectural Patterns

**Clean Architecture (both client & server)**
```
UI (Compose) → ViewModel → Use Case → Repository Interface
                                            ↓
                                    Repository Impl → Ktor Client → HTTPS
                                                       ↓
                                    ═══ network boundary ═══
                                                       ↓
                                    Ktor Route → Use Case → Domain Model
                                                       ↓
                                    Repository Interface → Firestore Data Source
```

**Error Handling**: `AppResult<T, E>` sealed class (Success/Error) — **no exceptions in domain layer**

**Navigation**: Type-safe `SkillXRoute` sealed class + `AppNavigator` (StateFlow back stack) + `SkillXNavGraph`

**Auth**: Server issues/validates JWT (HMAC256); client stores tokens securely (Multiplatform-Settings / Keychain / Keystore)

**Matching**: Server-side only — client never pulls user collection

**Lessons**: Atomic Firestore transaction on completion (validate → deduct learner −1 → credit teacher +1 → write `pointTransactions` ledger)

**Payments**: Pluggable provider architecture (Stripe, RevenueCat, M-Pesa, Bank Transfer) — new provider = one new file

---

## Major Differences

| Category | `development1` | `development2` |
|----------|----------------|----------------|
| **Modules** | 1 (`:app`) | 5 (`:shared`, `:server`, `:androidApp`, `:iosApp`, `:functions`) |
| **Kotlin** | 2.2.10 | **2.4.20** |
| **Ktor** | — | **3.6.0** (client + server) |
| **Compose** | Android only | **Compose Multiplatform 1.12.1** (Android + iOS + Desktop) |
| **DI** | — | **Koin 4.2.2** |
| **Serialization** | — | **kotlinx-serialization 1.11.0** |
| **Database** | Direct Firestore | Server-only Firestore + SQLDelight (offline cache) |
| **Background Jobs** | — | **JobRunr 8.8.2** (TiDB for job metadata) |
| **Testing** | JUnit/Espresso | **Turbine 1.2.0**, MockK, kotlinx-coroutines-test |
| **Navigation** | String `when(screen)` | **Type-safe sealed routes** |
| **Auth Flow** | Firebase Auth client | **JWT from server**, secure storage |
| **Point Economy** | Mutable `points` field | **Ledger (`pointTransactions`)** + atomic transfers |
| **Security** | Client-authoritative | **Server-authoritative** (all 4 fixes) |
| **Secrets** | `google-services.json` committed | **Comprehensive `.gitignore`**, secrets removed from history |

---

## Security Fixes in `development2`

| Vulnerability | `development1` | `development2` Fix |
|---------------|----------------|-------------------|
| Starting points | Client writes `points: 5` on signup | Server `RegisterUserUseCase` grants initial balance |
| Duplicate lesson request | Client queries locally | Server `CreateLessonRequestUseCase` — authoritative check in transaction |
| Purchase crediting | Client increments own `points` | Server `VerifyPurchaseUseCase` — atomic credit + ledger entry |
| Rating dedup | Client checks composite key | Server `SubmitRatingUseCase` — enforces uniqueness |
| Protected routes | Direct Firestore access | **JWT required** on all `/api/v1/*` routes; `userId` from token only |

---

## Getting Started

### Prerequisites
- JDK 21 (Gradle toolchain via Foojay resolver)
- Android Studio Ladybug+ / Xcode 15+
- Firebase project (Auth, Firestore)
- RevenueCat account (for IAP)
- Stripe account (optional, for web payments)
- TiDB Cloud (or MySQL-compatible) for JobRunr job storage

### Environment Variables

**Server** (`server/src/main/resources/application.conf` uses `${?ENV_VAR}`):
```bash
JWT_SECRET=your-hmac256-secret
JWT_ISSUER=https://your-domain.com
JWT_AUDIENCE=https://your-domain.com/api
FIREBASE_SERVICE_ACCOUNT_PATH=/path/to/service-account.json
STRIPE_SECRET_KEY=sk_...
STRIPE_WEBHOOK_SECRET=whsec_...
PORT=8080
```

**Shared Client** (`shared/src/commonMain/kotlin/com/skillx/network/configuration/ApiConfiguration.kt`):
```bash
API_BASE_URL=https://your-api-domain.com/api/v1
```

### Build & Run

```bash
# Clone and checkout development2
git clone <repo-url>
cd skillx
git checkout development2

# Server
./gradlew :server:run

# Android
./gradlew :androidApp:installDebug

# iOS (from iosApp/)
open iosApp/iosApp.xcworkspace  # Xcode → Run

# Desktop (Compose)
./gradlew :shared:runDesktop

# All tests
./gradlew :shared:allTests :server:test
```

---

## Project Structure (development2)

```
shared/src/commonMain/kotlin/com/skillx/
├── core/                    # AppResult, AppError, Clock, Logger, Validators, IDs
├── network/                 # SkillXHttpClient, AuthTokenProvider, ApiConfiguration
├── designsystem/            # Theme tokens + reusable Compose components
├── navigation/              # SkillXRoute, AppNavigator, SkillXNavGraph
├── storage/                 # SecureTokenStorage (expect/actual), SkillXDatabase (SQLDelight)
└── features/
    ├── authentication/      # Login/Signup/Welcome screens + use cases
    ├── users/               # Home, Profile, EditProfile
    ├── skills/              # Skills management
    ├── matching/            # Find matches, MatchProfile (WebSocket)
    ├── lessons/             # Request, accept, complete lessons
    ├── points/              # Balance + history (ledger)
    ├── ratings/             # Submit/load ratings
    ├── reports/             # Report users
    ├── payments/            # Purchase points (RevenueCat/Stripe)
    ├── notifications/       # Inbox
    ├── safety/              # Guidelines
    └── howitworks/          # Onboarding

server/src/main/kotlin/com/skillx/server/
├── Application.kt           # Bootstrap only
├── plugins/                 # Auth, Routing, CORS, RateLimit, WebSockets, etc.
├── configuration/           # JwtConfig, FirebaseConfig, StripeConfig, JobRunrConfig
├── core/                    # AppResult, Exceptions, RequestLogger
├── infrastructure/          # Firestore, JWT, PasswordHasher, Stripe, FirebaseAdmin, Jobs
└── features/                # Per-feature: domain → application → infrastructure → interfaces
```

---

## API Endpoints (development2)

All under `/api/v1`, JWT required except `/auth/*`:

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/auth/register` | Register new user (returns JWT) |
| `POST` | `/auth/login` | Login (returns JWT) |
| `GET` | `/users/me` | Current user profile |
| `GET` | `/users/{userId}` | Public user profile |
| `PUT` | `/users/me` | Update profile |
| `GET` | `/skills` | Get user's teach/learn skills |
| `PUT` | `/skills` | Update skills |
| `GET` | `/matches` | Find skill matches |
| `WS` | `/matches/stream` | Live match updates |
| `POST` | `/lessons` | Create lesson request |
| `GET` | `/lessons` | List my lesson requests |
| `PUT` | `/lessons/{id}/accept` | Accept request (teacher only) |
| `PUT` | `/lessons/{id}/complete` | Complete lesson (atomic points transfer) |
| `GET` | `/points/balance` | Current point balance |
| `GET` | `/points/history` | Point transaction ledger |
| `POST` | `/ratings` | Submit rating |
| `GET` | `/ratings/{userId}/summary` | Rating summary |
| `POST` | `/reports` | Report user |
| `GET` | `/payments/packages` | Available point packages |
| `POST` | `/payments/verify` | Verify purchase (RevenueCat/Stripe) |
| `POST` | `/webhooks/{provider}` | Payment webhooks |

---

## Firestore Collections

| Collection | Description | Access |
|------------|-------------|--------|
| `users` | User profiles, skills, points | Server only |
| `lessonRequests` | Lesson requests with status | Server only |
| `ratings` | Lesson ratings (composite key: `lessonId_uid`) | Server only |
| `reports` | User reports | Server only |
| `pointTransactions` | **Ledger** — every point movement | Server only |
| `purchases` | Verified purchase records | Server only |

**Security Rules**: Deny all direct client access (server is sole writer/reader).

---

## Contributing

1. Work on `development2` branch
2. Follow clean architecture: domain → use case → repository → implementation
3. No Firebase/Ktor/Compose imports in `domain/` packages
4. All new errors extend `AppError`; all operations return `AppResult`
5. Write tests for use cases (Turbine for flows, MockK for mocks)

---

## License

Proprietary — SkillX team.