SkillX/
├── README.md
├── settings.gradle.kts
├── gradlew / gradlew.bat
├── build-logic/
│   └── convention/src/main/kotlin/
│       ├── KotlinMultiplatformConventionPlugin.kt
│       ├── AndroidApplicationConventionPlugin.kt
│       └── KtorServerConventionPlugin.kt
│
├── shared/                                          # KMP client-shared module
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/com/skillx/
│       │   ├── core/
│       │   │   ├── result/AppResult.kt
│       │   │   ├── error/
│       │   │   │   ├── AppError.kt
│       │   │   │   ├── NetworkError.kt
│       │   │   │   ├── AuthenticationError.kt
│       │   │   │   ├── AuthorizationError.kt
│       │   │   │   ├── ValidationError.kt
│       │   │   │   ├── NotFoundError.kt
│       │   │   │   ├── ConflictError.kt
│       │   │   │   └── InsufficientPointsError.kt
│       │   │   ├── validation/
│       │   │   │   ├── EmailValidator.kt
│       │   │   │   └── PasswordValidator.kt
│       │   │   ├── time/Clock.kt
│       │   │   ├── identifiers/
│       │   │   │   ├── UserId.kt
│       │   │   │   ├── LessonId.kt
│       │   │   │   └── SkillId.kt
│       │   │   ├── extensions/
│       │   │   │   ├── StringExtensions.kt
│       │   │   │   └── CollectionExtensions.kt
│       │   │   └── logging/Logger.kt
│       │   │
│       │   ├── network/
│       │   │   ├── client/SkillXHttpClient.kt
│       │   │   ├── authentication/
│       │   │   │   ├── AuthTokenProvider.kt
│       │   │   │   └── BearerTokenPlugin.kt
│       │   │   ├── serialization/NetworkJson.kt
│       │   │   ├── error/ApiErrorMapper.kt
│       │   │   └── configuration/ApiConfiguration.kt
│       │   │
│       │   ├── storage/
│       │   │   ├── settings/SecureTokenStorage.kt      # Multiplatform-Settings, expect/actual
│       │   │   └── database/SkillXDatabase.sq           # SQLDelight — offline cache ONLY
│       │   │
│       │   ├── designsystem/
│       │   │   ├── theme/
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Type.kt
│       │   │   │   ├── Shape.kt
│       │   │   │   └── Theme.kt
│       │   │   └── components/
│       │   │       ├── SectionCard.kt
│       │   │       ├── PrimaryButton.kt
│       │   │       └── SecondaryButton.kt
│       │   │
│       │   ├── navigation/
│       │   │   ├── route/SkillXRoute.kt
│       │   │   ├── destination/SkillXDestination.kt
│       │   │   ├── navigator/AppNavigator.kt
│       │   │   └── graph/SkillXNavGraph.kt
│       │   │
│       │   └── features/
│       │       │
│       │       ├── authentication/
│       │       │   ├── domain/
│       │       │   │   ├── model/
│       │       │   │   │   ├── AuthSession.kt
│       │       │   │   │   └── AuthCredentials.kt
│       │       │   │   ├── repository/AuthRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── RegisterUserUseCase.kt
│       │       │   │       ├── LoginUserUseCase.kt
│       │       │   │       ├── LogoutUserUseCase.kt
│       │       │   │       └── ObserveAuthSessionUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/
│       │       │   │   │   ├── LoginRequestDto.kt
│       │       │   │   │   ├── RegisterRequestDto.kt
│       │       │   │   │   └── AuthResponseDto.kt
│       │       │   │   ├── mapper/AuthResponseMapper.kt
│       │       │   │   ├── remote/AuthApi.kt
│       │       │   │   └── repository/AuthRepositoryImpl.kt
│       │       │   └── presentation/
│       │       │       ├── welcome/WelcomeScreen.kt
│       │       │       ├── login/
│       │       │       │   ├── LoginScreen.kt
│       │       │       │   ├── LoginViewModel.kt
│       │       │       │   ├── LoginUiState.kt
│       │       │       │   └── LoginEvent.kt
│       │       │       └── signup/
│       │       │           ├── SignUpScreen.kt
│       │       │           ├── SignUpViewModel.kt
│       │       │           ├── SignUpUiState.kt
│       │       │           └── SignUpEvent.kt
│       │       │
│       │       ├── users/
│       │       │   ├── domain/
│       │       │   │   ├── model/UserProfile.kt
│       │       │   │   ├── repository/UserRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── LoadCurrentUserUseCase.kt
│       │       │   │       └── UpdateProfileUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/
│       │       │   │   │   ├── UserProfileDto.kt
│       │       │   │   │   └── UpdateProfileRequestDto.kt
│       │       │   │   ├── mapper/UserProfileMapper.kt
│       │       │   │   ├── remote/UserApi.kt
│       │       │   │   └── repository/UserRepositoryImpl.kt
│       │       │   └── presentation/
│       │       │       ├── home/
│       │       │       │   ├── HomeScreen.kt
│       │       │       │   ├── HomeViewModel.kt
│       │       │       │   └── HomeUiState.kt
│       │       │       ├── view/
│       │       │       │   ├── ProfileScreen.kt
│       │       │       │   ├── ProfileViewModel.kt
│       │       │       │   └── ProfileUiState.kt
│       │       │       └── edit/
│       │       │           ├── EditProfileScreen.kt
│       │       │           ├── EditProfileViewModel.kt
│       │       │           └── EditProfileUiState.kt
│       │       │
│       │       ├── skills/
│       │       │   ├── domain/
│       │       │   │   ├── model/Skill.kt
│       │       │   │   ├── repository/SkillRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── AddTeachingSkillUseCase.kt
│       │       │   │       ├── RemoveTeachingSkillUseCase.kt
│       │       │   │       ├── AddLearningSkillUseCase.kt
│       │       │   │       └── RemoveLearningSkillUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/SkillListRequestDto.kt
│       │       │   │   ├── remote/SkillApi.kt
│       │       │   │   └── repository/SkillRepositoryImpl.kt
│       │       │   └── presentation/skills/
│       │       │       ├── SkillsScreen.kt
│       │       │       ├── SkillsViewModel.kt
│       │       │       ├── SkillsUiState.kt
│       │       │       └── component/SkillRow.kt
│       │       │
│       │       ├── matching/
│       │       │   ├── domain/
│       │       │   │   ├── model/SkillMatch.kt
│       │       │   │   ├── repository/MatchRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── FindSkillMatchesUseCase.kt
│       │       │   │       └── ObserveLiveMatchesUseCase.kt        # WebSocket stream
│       │       │   ├── data/
│       │       │   │   ├── dto/SkillMatchDto.kt
│       │       │   │   ├── mapper/SkillMatchMapper.kt
│       │       │   │   ├── remote/
│       │       │   │   │   ├── MatchApi.kt
│       │       │   │   │   └── MatchSocketClient.kt              # Ktor WebSockets
│       │       │   │   └── repository/MatchRepositoryImpl.kt
│       │       │   └── presentation/
│       │       │       ├── list/
│       │       │       │   ├── MatchesScreen.kt
│       │       │       │   ├── MatchesViewModel.kt
│       │       │       │   ├── MatchesUiState.kt
│       │       │       │   └── component/MatchCard.kt
│       │       │       └── detail/
│       │       │           ├── MatchProfileScreen.kt
│       │       │           ├── MatchProfileViewModel.kt
│       │       │           └── MatchProfileUiState.kt
│       │       │
│       │       ├── lessons/
│       │       │   ├── domain/
│       │       │   │   ├── model/
│       │       │   │   │   ├── LessonRequest.kt
│       │       │   │   │   └── LessonStatus.kt
│       │       │   │   ├── repository/LessonRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── CreateLessonRequestUseCase.kt
│       │       │   │       ├── AcceptLessonRequestUseCase.kt
│       │       │   │       ├── CompleteLessonUseCase.kt
│       │       │   │       └── ObserveMyLessonsUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/
│       │       │   │   │   ├── CreateLessonRequestDto.kt
│       │       │   │   │   └── LessonRequestResponseDto.kt
│       │       │   │   ├── mapper/LessonRequestMapper.kt
│       │       │   │   ├── remote/LessonApi.kt
│       │       │   │   └── repository/LessonRepositoryImpl.kt
│       │       │   └── presentation/
│       │       │       ├── request/
│       │       │       │   ├── RequestLessonScreen.kt
│       │       │       │   ├── RequestLessonViewModel.kt
│       │       │       │   └── RequestLessonUiState.kt
│       │       │       └── list/
│       │       │           ├── LessonRequestsScreen.kt
│       │       │           ├── LessonRequestsViewModel.kt
│       │       │           ├── LessonRequestsUiState.kt
│       │       │           └── component/LessonRequestCard.kt
│       │       │
│       │       ├── points/
│       │       │   ├── domain/
│       │       │   │   ├── model/
│       │       │   │   │   ├── PointBalance.kt
│       │       │   │   │   └── PointTransaction.kt
│       │       │   │   ├── repository/PointRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── LoadPointBalanceUseCase.kt
│       │       │   │       └── ObservePointHistoryUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/PointBalanceDto.kt
│       │       │   │   ├── remote/PointApi.kt
│       │       │   │   └── repository/PointRepositoryImpl.kt
│       │       │   └── presentation/history/
│       │       │       ├── PointHistoryScreen.kt
│       │       │       ├── PointHistoryViewModel.kt
│       │       │       └── PointHistoryUiState.kt
│       │       │
│       │       ├── ratings/
│       │       │   ├── domain/
│       │       │   │   ├── model/Rating.kt
│       │       │   │   ├── repository/RatingRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── SubmitRatingUseCase.kt
│       │       │   │       └── LoadRatingSummaryUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/SubmitRatingRequestDto.kt
│       │       │   │   ├── mapper/RatingMapper.kt
│       │       │   │   ├── remote/RatingApi.kt
│       │       │   │   └── repository/RatingRepositoryImpl.kt
│       │       │   └── presentation/
│       │       │       ├── rate/
│       │       │       │   ├── RateLessonScreen.kt
│       │       │       │   ├── RateLessonViewModel.kt
│       │       │       │   └── RateLessonUiState.kt
│       │       │       └── component/RatingSummaryBar.kt
│       │       │
│       │       ├── reports/
│       │       │   ├── domain/
│       │       │   │   ├── model/
│       │       │   │   │   ├── UserReport.kt
│       │       │   │   │   └── ReportReason.kt
│       │       │   │   ├── repository/ReportRepository.kt
│       │       │   │   └── usecase/SubmitReportUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/SubmitReportRequestDto.kt
│       │       │   │   ├── remote/ReportApi.kt
│       │       │   │   └── repository/ReportRepositoryImpl.kt
│       │       │   └── presentation/report/
│       │       │       ├── ReportUserScreen.kt
│       │       │       ├── ReportUserViewModel.kt
│       │       │       └── ReportUserUiState.kt
│       │       │
│       │       ├── payments/
│       │       │   ├── domain/
│       │       │   │   ├── model/
│       │       │   │   │   ├── PointPackage.kt
│       │       │   │   │   └── PurchaseReceipt.kt
│       │       │   │   ├── repository/PaymentRepository.kt
│       │       │   │   └── usecase/
│       │       │   │       ├── LoadPointPackagesUseCase.kt
│       │       │   │       └── PurchasePointPackageUseCase.kt
│       │       │   ├── data/
│       │       │   │   ├── dto/VerifyPurchaseRequestDto.kt
│       │       │   │   ├── remote/
│       │       │   │   │   ├── PaymentApi.kt
│       │       │   │   │   └── RevenueCatClient.kt
│       │       │   │   └── repository/PaymentRepositoryImpl.kt
│       │       │   └── presentation/purchase/
│       │       │       ├── PurchasePointsScreen.kt
│       │       │       ├── PurchasePointsViewModel.kt
│       │       │       └── PurchasePointsUiState.kt
│       │       │
│       │       ├── notifications/
│       │       │   ├── domain/
│       │       │   │   ├── model/AppNotification.kt
│       │       │   │   └── repository/NotificationRepository.kt
│       │       │   ├── data/repository/NotificationRepositoryImpl.kt
│       │       │   └── presentation/inbox/
│       │       │       ├── NotificationInboxScreen.kt
│       │       │       └── NotificationInboxViewModel.kt
│       │       │
│       │       ├── safety/
│       │       │   └── presentation/guide/
│       │       │       ├── SafetyScreen.kt
│       │       │       └── component/SafetyRuleCard.kt
│       │       │
│       │       └── howitworks/
│       │           └── presentation/
│       │               ├── HowSkillXWorksScreen.kt
│       │               └── component/HowItWorksStepCard.kt
│       │
│       ├── commonTest/kotlin/com/skillx/features/
│       │   ├── matching/domain/usecase/FindSkillMatchesUseCaseTest.kt
│       │   └── lessons/domain/usecase/CreateLessonRequestUseCaseTest.kt
│       │
│       ├── androidMain/kotlin/com/skillx/
│       │   ├── storage/AndroidSecureTokenStorage.kt
│       │   └── platform/AndroidPlatform.kt
│       │
│       └── iosMain/kotlin/com/skillx/
│           ├── storage/IosSecureTokenStorage.kt
│           └── platform/IosPlatform.kt
│
├── androidApp/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── kotlin/com/skillx/android/
│           ├── SkillXApplication.kt
│           └── MainActivity.kt                         # < 100 lines: theme + NavHost only
│
├── iosApp/
│   └── iosApp/
│       ├── iOSApp.swift
│       └── ContentView.swift
│
└── server/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── kotlin/com/skillx/server/
        │   │   ├── Application.kt                       # bootstrap only
        │   │   │
        │   │   ├── configuration/
        │   │   │   ├── AppConfig.kt
        │   │   │   ├── FirebaseConfig.kt
        │   │   │   ├── JwtConfig.kt
        │   │   │   ├── StripeConfig.kt
        │   │   │   └── JobRunrConfig.kt
        │   │   │
        │   │   ├── plugins/
        │   │   │   ├── AuthenticationPlugin.kt
        │   │   │   ├── ContentNegotiationPlugin.kt
        │   │   │   ├── StatusPagesPlugin.kt
        │   │   │   ├── RoutingPlugin.kt
        │   │   │   ├── WebSocketsPlugin.kt
        │   │   │   ├── CorsPlugin.kt
        │   │   │   ├── RateLimitPlugin.kt
        │   │   │   └── MonitoringPlugin.kt
        │   │   │
        │   │   ├── core/
        │   │   │   ├── result/AppResult.kt
        │   │   │   ├── error/
        │   │   │   │   ├── AppException.kt
        │   │   │   │   ├── AuthenticationException.kt
        │   │   │   │   ├── AuthorizationException.kt
        │   │   │   │   ├── ValidationException.kt
        │   │   │   │   ├── NotFoundException.kt
        │   │   │   │   ├── ConflictException.kt
        │   │   │   │   └── InsufficientPointsException.kt
        │   │   │   └── logging/RequestLogger.kt
        │   │   │
        │   │   ├── infrastructure/
        │   │   │   ├── firestore/
        │   │   │   │   ├── FirestoreClientProvider.kt
        │   │   │   │   └── FirestoreTransactionRunner.kt
        │   │   │   ├── authentication/
        │   │   │   │   ├── JwtTokenService.kt
        │   │   │   │   └── PasswordHasher.kt
        │   │   │   ├── payments/
        │   │   │   │   ├── StripeClient.kt
        │   │   │   │   └── StripeWebhookVerifier.kt
        │   │   │   ├── notifications/
        │   │   │   │   └── FirebaseAdminPushSender.kt
        │   │   │   └── jobs/
        │   │   │       ├── JobRunrScheduler.kt          # storage backend: decide + document (see master prompt §3)
        │   │   │       └── job/
        │   │   │           ├── ProcessLessonCompletionJob.kt
        │   │   │           ├── ReconcilePaymentJob.kt
        │   │   │           └── RefreshMatchesJob.kt
        │   │   │
        │   │   └── features/
        │   │       │
        │   │       ├── authentication/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/AuthenticatedPrincipal.kt
        │   │       │   │   └── repository/AuthRepository.kt
        │   │       │   ├── application/
        │   │       │   │   ├── usecase/
        │   │       │   │   │   ├── RegisterUserUseCase.kt
        │   │       │   │   │   └── LoginUserUseCase.kt
        │   │       │   │   └── mapper/AuthResponseMapper.kt
        │   │       │   ├── infrastructure/firestore/FirestoreAuthDataSource.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── AuthRoutes.kt
        │   │       │       ├── request/
        │   │       │       │   ├── RegisterRequest.kt
        │   │       │       │   └── LoginRequest.kt
        │   │       │       └── response/AuthResponse.kt
        │   │       │
        │   │       ├── users/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/User.kt
        │   │       │   │   └── repository/UserRepository.kt
        │   │       │   ├── application/
        │   │       │   │   ├── usecase/
        │   │       │   │   │   ├── LoadUserProfileUseCase.kt
        │   │       │   │   │   └── UpdateUserProfileUseCase.kt
        │   │       │   │   └── mapper/UserResponseMapper.kt
        │   │       │   ├── infrastructure/firestore/
        │   │       │   │   ├── FirestoreUserDataSource.kt
        │   │       │   │   └── UserDocumentMapper.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── UserRoutes.kt
        │   │       │       ├── request/UpdateUserRequest.kt
        │   │       │       └── response/UserResponse.kt
        │   │       │
        │   │       ├── skills/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/Skill.kt
        │   │       │   │   └── repository/SkillRepository.kt
        │   │       │   ├── application/usecase/
        │   │       │   │   ├── AddTeachingSkillUseCase.kt
        │   │       │   │   ├── RemoveTeachingSkillUseCase.kt
        │   │       │   │   ├── AddLearningSkillUseCase.kt
        │   │       │   │   └── RemoveLearningSkillUseCase.kt
        │   │       │   ├── infrastructure/firestore/FirestoreSkillDataSource.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── SkillRoutes.kt
        │   │       │       ├── request/UpdateSkillsRequest.kt
        │   │       │       └── response/SkillsResponse.kt
        │   │       │
        │   │       ├── matching/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/SkillMatch.kt
        │   │       │   │   ├── matcher/SkillMatcher.kt
        │   │       │   │   └── repository/MatchRepository.kt
        │   │       │   ├── application/
        │   │       │   │   ├── usecase/FindSkillMatchesUseCase.kt
        │   │       │   │   └── mapper/MatchResponseMapper.kt
        │   │       │   ├── infrastructure/firestore/FirestoreMatchDataSource.kt
        │   │       │   └── interfaces/
        │   │       │       ├── http/
        │   │       │       │   ├── MatchRoutes.kt
        │   │       │       │   └── response/SkillMatchResponse.kt
        │   │       │       └── websocket/MatchStreamSocket.kt
        │   │       │
        │   │       ├── lessons/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/
        │   │       │   │   │   ├── LessonRequest.kt
        │   │       │   │   │   └── LessonStatus.kt
        │   │       │   │   ├── service/LessonStateTransitionValidator.kt
        │   │       │   │   └── repository/LessonRepository.kt
        │   │       │   ├── application/
        │   │       │   │   ├── usecase/
        │   │       │   │   │   ├── CreateLessonRequestUseCase.kt
        │   │       │   │   │   ├── AcceptLessonRequestUseCase.kt
        │   │       │   │   │   ├── RejectLessonRequestUseCase.kt
        │   │       │   │   │   └── CompleteLessonUseCase.kt
        │   │       │   │   └── mapper/LessonResponseMapper.kt
        │   │       │   ├── infrastructure/firestore/
        │   │       │   │   ├── FirestoreLessonDataSource.kt
        │   │       │   │   └── LessonDocumentMapper.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── LessonRoutes.kt
        │   │       │       ├── request/CreateLessonRequestRequest.kt
        │   │       │       └── response/LessonRequestResponse.kt
        │   │       │
        │   │       ├── points/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/
        │   │       │   │   │   ├── PointBalance.kt
        │   │       │   │   │   └── PointTransaction.kt
        │   │       │   │   ├── service/PointTransferService.kt   # atomic-transfer business rules
        │   │       │   │   └── repository/
        │   │       │   │       ├── PointRepository.kt
        │   │       │   │       └── PointLedgerRepository.kt
        │   │       │   ├── application/usecase/
        │   │       │   │   ├── LoadPointBalanceUseCase.kt
        │   │       │   │   └── TransferPointsUseCase.kt
        │   │       │   ├── infrastructure/firestore/
        │   │       │   │   ├── FirestorePointDataSource.kt
        │   │       │   │   └── FirestorePointTransferTransaction.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── PointRoutes.kt
        │   │       │       └── response/
        │   │       │           ├── PointBalanceResponse.kt
        │   │       │           └── PointTransactionResponse.kt
        │   │       │
        │   │       ├── ratings/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/Rating.kt
        │   │       │   │   └── repository/RatingRepository.kt
        │   │       │   ├── application/usecase/
        │   │       │   │   ├── SubmitRatingUseCase.kt
        │   │       │   │   └── LoadRatingSummaryUseCase.kt
        │   │       │   ├── infrastructure/firestore/FirestoreRatingDataSource.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── RatingRoutes.kt
        │   │       │       ├── request/SubmitRatingRequest.kt
        │   │       │       └── response/RatingSummaryResponse.kt
        │   │       │
        │   │       ├── reports/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/
        │   │       │   │   │   ├── UserReport.kt
        │   │       │   │   │   └── ReportReason.kt
        │   │       │   │   └── repository/ReportRepository.kt
        │   │       │   ├── application/usecase/SubmitReportUseCase.kt
        │   │       │   ├── infrastructure/firestore/FirestoreReportDataSource.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── ReportRoutes.kt
        │   │       │       └── request/SubmitReportRequest.kt
        │   │       │
        │   │       ├── payments/
        │   │       │   ├── domain/
        │   │       │   │   ├── model/
        │   │       │   │   │   ├── PointPackage.kt
        │   │       │   │   │   └── PurchaseReceipt.kt
        │   │       │   │   └── repository/PaymentRepository.kt
        │   │       │   ├── application/usecase/
        │   │       │   │   ├── VerifyPurchaseUseCase.kt
        │   │       │   │   ├── HandleStripeWebhookUseCase.kt
        │   │       │   │   └── HandleRevenueCatWebhookUseCase.kt
        │   │       │   ├── infrastructure/
        │   │       │   │   ├── stripe/StripePaymentGateway.kt
        │   │       │   │   └── firestore/FirestorePurchaseDataSource.kt
        │   │       │   └── interfaces/http/
        │   │       │       ├── PaymentRoutes.kt
        │   │       │       ├── WebhookRoutes.kt
        │   │       │       └── request/VerifyPurchaseRequest.kt
        │   │       │
        │   │       └── notifications/
        │   │           ├── domain/
        │   │           │   ├── model/AppNotification.kt
        │   │           │   └── repository/NotificationRepository.kt
        │   │           ├── application/usecase/SendLessonNotificationUseCase.kt
        │   │           ├── infrastructure/firebase/FirebaseAdminNotificationSender.kt
        │   │           └── interfaces/event/LessonRequestCreatedListener.kt
        │   │
        │   └── resources/
        │       ├── application.conf
        │       └── logback.xml
        │
        └── test/kotlin/com/skillx/server/features/
            ├── points/application/usecase/TransferPointsUseCaseTest.kt
            └── lessons/application/usecase/CompleteLessonUseCaseTest.kt