package com.skillx.core.logging

/**
 * Multiplatform logging abstraction.
 * Actual implementations use platform-specific logging (Logcat on Android, NSLog on iOS).
 */
expect object Logger {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warn(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
