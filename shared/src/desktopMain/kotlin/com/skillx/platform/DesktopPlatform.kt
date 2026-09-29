package com.skillx.platform

actual class Platform {
    actual val name: String = "Desktop"
    actual val version: String = System.getProperty("os.version") ?: "unknown"
}
