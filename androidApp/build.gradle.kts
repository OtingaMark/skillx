plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.skillx.android"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.skillx.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Public OAuth client IDs (not secrets) — override via -PGOOGLE_OAUTH_SERVER_CLIENT_ID=
        // or gradle.properties. The server independently verifies every token against the
        // provider's own keys, so a wrong/missing client ID just fails sign-in, nothing worse.
        buildConfigField(
            "String",
            "GOOGLE_OAUTH_SERVER_CLIENT_ID",
            "\"${project.findProperty("GOOGLE_OAUTH_SERVER_CLIENT_ID") ?: ""}\""
        )
        buildConfigField(
            "String",
            "LINKEDIN_OAUTH_CLIENT_ID",
            "\"${project.findProperty("LINKEDIN_OAUTH_CLIENT_ID") ?: ""}\""
        )
        buildConfigField("String", "LINKEDIN_OAUTH_REDIRECT_URI", "\"skillx://oauth/linkedin/callback\"")

        // Server base URL — override via -PAPI_BASE_URL= or gradle.properties per environment.
        // Uses the host machine's real LAN IP so the physical device talks to it over WiFi —
        // no dependency on `adb reverse`, which drops every time the USB connection resets.
        // Phone and host must be on the same network; update this IP if the host's changes.
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"${project.findProperty("API_BASE_URL") ?: "http://192.168.100.14:8080"}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // DI
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // AppModule.kt references these :shared-originating types directly (Settings, HttpClient) —
    // :shared's own dependencies are `implementation`-scoped, so they don't transitively reach here.
    implementation(libs.multiplatform.settings)
    implementation(libs.multiplatform.settings.no.arg)
    implementation(libs.ktor.client.core)

    // RevenueCat (Android-specific IAP)
    implementation("com.revenuecat.purchases:purchases:10.15.1")

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
