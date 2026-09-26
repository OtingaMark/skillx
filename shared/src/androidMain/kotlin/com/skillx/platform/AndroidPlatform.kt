package com.skillx.platform

actual class AndroidPlatform {
    actual val name: String = "Android"
    actual val version: String = android.os.Build.VERSION.RELEASE
}
