package com.skillx.platform

actual class Platform {
    actual val name: String = "Android"
    actual val version: String = android.os.Build.VERSION.RELEASE
}
