# SkillX — Full-Stack Kotlin Decomposition Master Prompt

## 0. Source of Truth

You are given one file: `MainActivity.kt`, package `com.skillx.app`, ~4,737 lines. It currently contains, in one file: theme/design tokens, a manual `when(screen)` string-based navigator, every screen as a `@Composable`, all Firebase Auth calls, all Firestore reads/writes/transactions across four collections (`users`, `lessonRequests`, `ratings`, `reports`), all RevenueCat purchase logic, and all validation/matching helper functions.

Your job is to decompose this single file into the target architecture in Section 4, following the target folder structure delivered separately ("SkillX Master Folder Structure"), without silently changing product behavior.

## 1. Role

Act simultaneously as: Principal Kotlin/KMP Architect, Senior Ktor Backend Engineer, Senior Android/Compose Multiplatform Engineer, Senior iOS/KMP Engineer, Firestore Data Modeler, Security/Authorization Engineer, Concurrency Engineer, and Testing Architect.

## 2. Non-Negotiable Rules

1. **One file, one responsibility, one reason to change.** Not "one function per file" — one cohesive concept per file. A `data class LessonRequest(...)` stays one file. `LoginScreen.kt` + `LoginViewModel.kt` + `LoginUiState.kt` are three files because they are three different responsibilities (render, coordinate, hold state), not because of a file-count target.
2. **No god files.** No `Utils.kt`, `Helpers.kt`, `Manager.kt`, `Common.kt` dumping grounds.
3. **No placeholders.** No `TODO`, no `// implement later`, no stub bodies, no fake return values. If something genuinely cannot be completed, STOP and report it as a blocker (Section 11) instead of faking it.
4. **The client is never authoritative.** Every write that currently happens directly from Compose → Firestore must move behind an HTTP call to Ktor. The mobile apps stop importing `FirebaseFirestore` entirely once this refactor is done — Firestore access lives only in the server's `infrastructure/firestore` layer.
5. **Preserve existing product behavior exactly** unless a change is explicitly a security fix (Section 6) — then implement the fix and flag it as a deliberate deviation.
6. **Domain code must not import** `androidx.compose.*`, `android.*`, `com.google.firebase.*`, or `io.ktor.*`. Domain models are plain Kotlin.
7. **No silent breaking changes.** When you must deviate from the original file's behavior (e.g., security fixes, design spec changes, unavoidable platform limitations), you MUST include the “Reason” in your commit message and explicitly link to this section. This allows for review and, if necessary, rollback.

## 3. Confirmed Technology Stack

| Concern | Choice | Notes |
|---|---|---|
| Client UI | Kotlin Multiplatform + Compose Multiplatform | Android + iOS from one shared UI/business layer |
| Backend | Ktor (JVM) | Non-blocking, coroutine-native |
| Primary datastore | Cloud Firestore only | No Realtime Database. Do not introduce Postgres/Exposed as a second source of truth for business data — see the JobRunr exception below |
| Client local cache | SQLDelight | Offline cache only, never a second source of truth. Server response always wins on sync |
| Auth | `ktor-server-auth-jwt` (server) + secure token storage on the client (Multiplatform-Settings, or platform Keychain/Keystore) | Server issues and verifies JWTs; client never decides its own identity or role |
| Real-time matching | Kotlin Coroutines/Flow + Ktor WebSockets | Matching computation stays server-side; the socket only streams results |
| Background jobs | JobRunr or Quartz | ⚠️ Open technical question, do not silently resolve it: JobRunr/Quartz need their own job-storage backend (SQL, Redis, or Mongo) — Firestore is not a supported JobRunr `StorageProvider`. Pick one dedicated, minimal store for job metadata only; it must never hold business/domain data. Report this choice explicitly in your final output. **Resolved in Addendum B below: TiDB.** |
| Payments | Pluggable provider architecture (Stripe, M-Pesa, bank transfer, RevenueCat, and any future provider) | See Addendum A below — replaces any single-vendor assumption |
| Push notifications | Firebase Admin SDK (server-side only) | Never ship Admin SDK credentials to the client |

## 4. Target Architecture Shape

```
Compose Multiplatform UI (Android/iOS)
        ↓ event
   ViewModel (shared, commonMain)
        ↓
     Use Case (shared, commonMain — pure Kotlin)
        ↓
  Repository interface (shared)
        ↓
  Repository impl → Ktor Client → HTTPS
        ↓
   ═══════════ network boundary ═══════════
        ↓
     Ktor route (thin — no business logic)
        ↓ Request DTO
     JWT auth + authorization check
        ↓
     Use Case (server, pure Kotlin)
        ↓
     Domain model + domain rules
        ↓
  Repository interface (server)
        ↓
  Repository impl → Firestore data source
        ↓
       Firestore
```

Follow this shape for every feature. No exceptions, no "just this once the screen calls Firestore directly."

## 5. Phase 0 — Build Your Own Inventory First

Before writing any target file, re-derive a current, accurate inventory of the source: every `@Composable`, every top-level `fun`, every `data class`, every Firestore collection/field read or written, every RevenueCat call, every navigation transition, every piece of mutable state. Do not trust a stale line-number table (including the one below) if the pasted file has since changed — verify against what you actually have.

**Known component inventory** (from the current file, as a starting reference):

| Source component | Client target (shared KMP) | Server target (Ktor) |
|---|---|---|
| `REVENUECAT_API_KEY` const | `network/configuration/ApiConfiguration.kt` (public config only) | `configuration/StripeConfig.kt` / payments infra (secret keys, server-only, from env) |
| `SkillXLightColors`, `SkillXTheme` | `designsystem/theme/Color.kt`, `Theme.kt` | — |
| `SectionCard`, `PrimaryAction`, `SecondaryAction` | `designsystem/components/*` | — |
| `MainActivity.onCreate` + `initializeRevenueCat` | `androidApp/.../SkillXApplication.kt`, thin `MainActivity.kt` | — |
| `SkillXApp()` root `when(screen)` state machine | `navigation/graph/SkillXNavGraph.kt`, `navigation/route/`, `navigation/destination/`, `navigation/navigator/AppNavigator.kt` | — |
| `loginToRevenueCat` / `logoutFromRevenueCat` | `features/payments/data/remote/RevenueCatClient.kt` | — |
| `WelcomeScreen` | `features/authentication/presentation/welcome/WelcomeScreen.kt` | — |
| `SignUpScreen` (incl. Firestore `users/{uid}` doc + initial 5 points) | `features/authentication/presentation/signup/*` (UI + form validation only) | `features/authentication/application/usecase/RegisterUserUseCase.kt` — the initial-5-points grant and user-document creation move server-side. Client no longer decides a user's starting balance. |
| `LoginScreen` | `features/authentication/presentation/login/*` | `LoginUserUseCase.kt`, `AuthRoutes.kt` |
| `HomeScreen` | `features/users/presentation/home/*` (or a dedicated `home` feature) | `LoadUserProfileUseCase.kt` |
| `RevenueCatScreen`, `loadPoints`, `buyPackage`, `pointsForProduct` | `features/payments/presentation/purchase/*`, `RevenueCatClient.kt` | `VerifyPurchaseUseCase.kt` — point-crediting on purchase moves server-side entirely; the client must never increment its own Firestore points field |
| `Context.findActivity()` | Android-specific `platform/AndroidPlatform.kt` | — |
| `ProfileScreen` | `features/users/presentation/view/*` | — |
| `EditProfileScreen` | `features/users/presentation/edit/*` | `UpdateUserProfileUseCase.kt` |
| `SkillsScreen`, `SkillRow` | `features/skills/presentation/skills/*` | `Add/RemoveTeachingSkillUseCase.kt`, `Add/RemoveLearningSkillUseCase.kt` |
| `SkillMatch` data class | `features/matching/domain/model/SkillMatch.kt` | `features/matching/domain/model/SkillMatch.kt` |
| `MatchesScreen` + `findMatchingSkill` query loop | `features/matching/presentation/list/*` (calls `MatchApi`/socket only) | `findMatchingSkill` matching logic moves entirely server-side: `features/matching/domain/matcher/SkillMatcher.kt` + `FindSkillMatchesUseCase.kt`. The client must stop pulling the entire `users` collection to compute matches locally. |
| `MatchCard`, `MatchProfileScreen` | `features/matching/presentation/{list,detail}/*` | — |
| `RequestLessonScreen` (incl. duplicate-active-request query) | `features/lessons/presentation/request/*` | `CreateLessonRequestUseCase.kt` owns the duplicate-request check — never trust a client-side "already requested" check alone |
| `LessonRequest` data class | `features/lessons/domain/model/LessonRequest.kt` | same, server-side |
| `LessonRequestsScreen`, `LessonRequestCard`, accept transition | `features/lessons/presentation/list/*` | `AcceptLessonRequestUseCase.kt` — validates the caller is actually the request's teacher before flipping status |
| `completeLesson()` (Firestore transaction: −1/+1/status) | client only calls `POST /lessons/{id}/complete` | **The single most security-critical extraction.** `features/lessons/application/usecase/CompleteLessonUseCase.kt` + `features/points/domain/service/PointTransferService.kt` + `infrastructure/firestore/FirestorePointTransferTransaction.kt`. Re-verify, server-side: caller is a participant, lesson status is `accepted`, learner balance ≥ 1, all inside one atomic Firestore transaction. Also write a `PointTransaction` ledger entry, not just a balance mutation. |
| `RatingSummary` | `features/ratings/presentation/component/RatingSummaryBar.kt` | `LoadRatingSummaryUseCase.kt` |
| `RateLessonScreen` (`ratingId = lessonId_uid` dedup) | `features/ratings/presentation/rate/*` | `SubmitRatingUseCase.kt` must re-enforce the composite-key uniqueness server-side — never rely on a client-side "already rated" check alone |
| `HowSkillXWorksScreen`, `SafetyScreen` | `features/howitworks/`, `features/safety/` presentation (static content) | none needed — no business logic here |
| `ReportUserScreen` | `features/reports/presentation/report/*` | `SubmitReportUseCase.kt` |
| `findMatchingSkill` | moves server-side (see matching row above) | `features/matching/domain/matcher/SkillMatcher.kt` |
| `isValidEmail`, `parseSkills` | `core/validation/*` (transport-level validation, shared) | `core` validation, duplicated on purpose — client validation is UX, server validation is the actual guarantee |
| `getSkillsFromFirestore` | removed from client entirely | becomes a private mapper inside `infrastructure/firestore/UserDocumentMapper.kt` — Firestore document shapes never leak past the server's infrastructure layer |

## 6. Security Fixes This Refactor Must Make (Not Optional)

The current code lets the client decide: its own starting point balance, whether it already has a pending lesson request, whether it already rated a lesson, and its own credited points after a purchase. All four must become server-authoritative decisions, re-validated on the server regardless of what the client believes. This is the primary reason the backend exists — treat it as the load-bearing requirement of the whole migration, not a nice-to-have.

Also required:

- JWT verified on every protected route; authenticated principal (uid) is the only source of "who is calling," never a body field like `requesterId` taken at face value.
- Authorization check per operation: only a lesson's two participants may view/accept/complete/rate it; only the request's teacher may accept it; a user may not report an identity they invented.
- Firestore Security Rules updated to deny direct client reads/writes on `users`, `lessonRequests`, `ratings`, `reports` — since the client no longer talks to Firestore, rules should lock that down rather than leave it open "just in case."

## 7. Firestore-Specific Rules

- Preserve existing collection names and field semantics (`users`, `lessonRequests`, `ratings`, `reports`) unless you document a deliberate migration.
- The point transfer (learner −1, teacher +1, lesson → completed) must remain one atomic Firestore transaction, server-side, with explicit handling for: insufficient balance, already-completed lesson, wrong lesson status, non-participant caller, concurrent double-completion.
- Add a `pointTransactions` ledger collection (or subcollection) so balances are auditable, not just a mutable integer.
- Centralize all Firestore access behind named data sources (`FirestoreUserDataSource`, `FirestoreLessonDataSource`, etc.) — never call `FirebaseFirestore.getInstance()` from inside a use case or route.

## 8. Client (KMP) Rules

- `commonMain` holds everything platform-independent: domain models, use cases, repositories, DTOs, mappers, ViewModels, UI state, and Compose Multiplatform screens where feasible.
- `androidMain`/`iosMain` hold only what genuinely differs per platform (secure token storage implementation, platform entry points).
- The UI layer only renders state and emits events — it never touches a repository or the network client directly.
- Feature-first packages (`features/<name>/{domain,data,presentation}`), not global `models/`, `viewmodels/`, `screens/` buckets.

## 9. Server (Ktor) Rules

- `Application.kt` only bootstraps; it must not contain routes or business logic.
- Routes are thin: request DTO → use case → response DTO. No Firestore calls, no business rules inside a `route {}` block.
- Each feature owns `domain/`, `application/`, `infrastructure/`, `interfaces/` — a feature's Firestore data source lives inside that feature, not in a shared `infrastructure/` grab-bag.
- Domain layer has zero Ktor/Firestore imports; infrastructure implements domain-defined repository interfaces.

## 10. Process — Do This In Order, Verify Before Moving On

1. Build the live component inventory (Phase 0).
2. Stand up the folder skeleton exactly as given in the Master Folder Structure deliverable.
3. Extract domain models (pure data classes, no Firebase types) for both client and server.
4. Extract server use cases + repository interfaces, one business operation per use case.
5. Extract server Firestore data sources + document mappers.
6. Wire Ktor routes (thin) + JWT auth plugin + authorization checks.
7. Extract client repositories + Ktor Client API classes (replacing every direct Firestore call with an HTTP call).
8. Extract client ViewModels + UI state/event types per screen.
9. Extract client Composable screens, wired to their ViewModel only.
10. Rebuild navigation as an explicit route/destination graph (replace the `when(screen)` string switch).
11. Wire background jobs (JobRunr/Quartz) for anything genuinely asynchronous (purchase reconciliation, notification dispatch) — do not force synchronous work into a job just to use the library.
12. Wire the pluggable payment providers and Firebase Admin push notifications behind their own infrastructure adapters (see Addendum A).
13. Write tests: point-transfer edge cases (0 balance, concurrent completion, wrong caller), duplicate-request prevention, duplicate-rating prevention, authorization failures, matching correctness.
14. Run a dependency audit: confirm no domain file imports Compose/Android/Firebase/Ktor, confirm the client has zero remaining Firestore imports.

## 11. Stop Conditions

If at any point completing a step would require faking a return value, skipping an authorization check, guessing an undocumented business rule, or silently picking between storage backends without saying so — stop and report it as:

> **BLOCKER — \<short title\>**
>
> **Problem:** ...
> **Why it matters:** ...
> **What currently depends on it:** ...
> **Recommended fix:** ...
> **Decision needed from you:** ...

Do not paper over it with a placeholder.

## 12. Final Output Required

When the migration is complete, produce: the final folder tree as actually built, an old-file → new-files mapping, the auth flow, the lesson-request flow, the lesson-completion flow (in full, since it's the critical path), the rating flow, the payment/webhook flow, the API endpoint inventory, the Firestore collection inventory, the chosen job-storage backend and why, and any behavior you could not preserve exactly.

---

# Addendum A — Pluggable Payment Providers

Must support Stripe, M-Pesa, bank transfer, RevenueCat, and any future provider — each addable as one new file extending a common interface, with no edits to existing code beyond a single registration line.

The contract every provider implements, and the registry that consumes them, never change when you add a new one:

```kotlin
// domain/payment/PaymentProvider.kt — the extension point, stable forever
interface PaymentProvider {
    val id: PaymentProviderId
    suspend fun initiate(command: InitiatePaymentCommand): PaymentInitiationResult
    suspend fun handleWebhook(payload: RawWebhookPayload): WebhookOutcome
    suspend fun refund(command: RefundCommand): RefundResult
}

@JvmInline value class PaymentProviderId(val value: String)

sealed interface PaymentInitiationResult {
    data class RedirectRequired(val checkoutUrl: String, val reference: PaymentReference) : PaymentInitiationResult
    data class AwaitingConfirmation(val reference: PaymentReference, val instructions: String) : PaymentInitiationResult
    data class Failed(val reason: String) : PaymentInitiationResult
}

sealed interface WebhookOutcome {
    data class Confirmed(val reference: PaymentReference, val userId: UserId, val amount: Money) : WebhookOutcome
    data class Failed(val reference: PaymentReference, val reason: String) : WebhookOutcome
    object Ignored : WebhookOutcome
}
```

```kotlin
// domain/payment/PaymentProviderRegistry.kt — never edited when adding a provider
class PaymentProviderRegistry(providers: List<PaymentProvider>) {
    private val byId = providers.associateBy { it.id }
    fun get(id: PaymentProviderId) = byId[id] ?: throw UnsupportedPaymentProviderException(id)
}
```

Each provider is genuinely one new file:

```kotlin
// infrastructure/payments/providers/MpesaProvider.kt
class MpesaProvider(private val config: MpesaConfig, private val http: HttpClient) : PaymentProvider {
    override val id = PaymentProviderId("mpesa")

    override suspend fun initiate(command: InitiatePaymentCommand): PaymentInitiationResult {
        // Daraja STK Push — phone gets a prompt, so this always returns AwaitingConfirmation
        val ref = daraja.stkPush(command.phoneNumber, command.amount, config)
        return PaymentInitiationResult.AwaitingConfirmation(ref, "Enter your M-Pesa PIN on your phone")
    }

    override suspend fun handleWebhook(payload: RawWebhookPayload): WebhookOutcome =
        daraja.parseCallback(payload) // -> Confirmed or Failed

    override suspend fun refund(command: RefundCommand): RefundResult = daraja.reverse(command)
}

// infrastructure/payments/providers/StripeProvider.kt      — RedirectRequired flow
// infrastructure/payments/providers/BankTransferProvider.kt — AwaitingConfirmation, reconciled by a job (below)
// infrastructure/payments/providers/RevenueCatProvider.kt   — App Store/Play Store IAP is now just another provider
```

**Wiring** — the one line that does get touched per new provider. This is the honest tradeoff: true zero-file-touch needs JVM `ServiceLoader`/classpath scanning instead of DI, which trades explicitness for magic. Stick with DI unless you specifically want that.

```kotlin
// configuration/PaymentModule.kt
val paymentModule = module {
    single<PaymentProvider> { StripeProvider(get()) }
    single<PaymentProvider> { MpesaProvider(get(), get()) }
    single<PaymentProvider> { BankTransferProvider(get()) }
    single<PaymentProvider> { RevenueCatProvider(get()) }
    single { PaymentProviderRegistry(getAll()) }   // getAll() collects every binding above automatically
}
```

Routes and points-crediting stay generic across every provider, present and future:

```kotlin
post("/payments/{providerId}/initiate") { /* looks up registry.get(providerId), never changes */ }
post("/webhooks/{providerId}") { /* same */ }
```

```kotlin
class ProcessPaymentWebhookUseCase(
    private val registry: PaymentProviderRegistry,
    private val creditPoints: CreditPointsForPaymentUseCase   // one place, all providers
) {
    suspend operator fun invoke(providerId: PaymentProviderId, raw: RawWebhookPayload) {
        when (val outcome = registry.get(providerId).handleWebhook(raw)) {
            is WebhookOutcome.Confirmed -> creditPoints(outcome.userId, outcome.amount, outcome.reference)
            is WebhookOutcome.Failed -> Unit // log + notify
            WebhookOutcome.Ignored -> Unit
        }
    }
}
```

That last part matters most: the points ledger is credited from one provider-agnostic use case. Adding PayPal or Flutterwave later never touches this file — it only ever gains a new `WebhookOutcome.Confirmed` from wherever it came from.

Each provider gets its own config file too (`StripeConfig.kt`, `MpesaConfig.kt`, `BankTransferConfig.kt`), loaded from its own env vars — so a new provider's secrets don't land in a shared config blob either.

---

# Addendum B — Firestore + TiDB (Job Storage Split)

JobRunr's own documentation lists TiDB by name as one of the MySQL-wire-compatible databases its built-in MySQL storage provider already supports, alongside Aurora MySQL — so this closes the storage-backend blocker flagged in Section 3 without needing a custom `StorageProvider` implementation.

**Boundary rule:** TiDB is invisible outside `infrastructure/jobs/`. No feature's domain, application, or other infrastructure code ever opens a TiDB connection — Firestore stays the only business-data source of truth; TiDB holds nothing but job-queue metadata.

```
server/infrastructure/jobs/
    TiDbDataSourceProvider.kt   # HikariCP DataSource, MySQL JDBC driver → TiDB
    JobRunrConfig.kt            # JobRunr.configure().useStorageProvider(...).useBackgroundJobServer()
    JobRunrScheduler.kt
    job/
        ProcessLessonCompletionJob.kt
        ReconcilePaymentJob.kt
        ReconcileBankTransferJob.kt   # confirms pending bank-transfer references
        RefreshMatchesJob.kt
```

If you're already running a TiDB Cloud cluster for other projects, a separate database inside that same cluster (e.g. `skillx_jobs`) is enough — no need to stand up a dedicated cluster just for job storage.

One thing worth a quick spike before you commit: TiDB speaks the MySQL wire protocol but isn't byte-for-byte MySQL, so run JobRunr's migration/DDL step against it once and confirm the four job tables create cleanly before wiring it into the real app.