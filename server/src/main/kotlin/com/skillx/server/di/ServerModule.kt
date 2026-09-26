package com.skillx.server.di

import com.skillx.server.configuration.FirebaseConfig
import com.skillx.server.configuration.JwtConfig
import com.skillx.server.infrastructure.authentication.JwtTokenService
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import org.koin.dsl.module

val serverModule = module {
    single { JwtConfig.fromEnvironment() }
    single { FirebaseConfig.fromEnvironment() }
    single { JwtTokenService(get()) }
    single { FirestoreClientProvider(get()) }
}
