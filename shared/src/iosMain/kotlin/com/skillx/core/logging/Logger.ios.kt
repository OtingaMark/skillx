package com.skillx.core.logging

import platform.Foundation.NSLog

actual object Logger {
    actual fun debug(tag: String, message: String) { NSLog("[$tag] DEBUG: $message") }
    actual fun info(tag: String, message: String) { NSLog("[$tag] INFO: $message") }
    actual fun warn(tag: String, message: String) { NSLog("[$tag] WARN: $message") }
    actual fun error(tag: String, message: String, throwable: Throwable?) { NSLog("[$tag] ERROR: $message ${throwable?.message ?: ""}") }
}
