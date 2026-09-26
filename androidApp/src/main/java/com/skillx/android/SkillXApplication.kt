package com.skillx.android

import android.app.Application
import com.skillx.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SkillXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SkillXApplication)
            modules(appModule)
        }
    }
}
