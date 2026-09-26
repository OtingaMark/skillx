package com.skillx.core.logging

actual object Logger {
    actual fun debug(tag: String, message: String) { println("[$tag] DEBUG: $message") }
    actual fun info(tag: String, message: String) { println("[$tag] INFO: $message") }
    actual fun warn(tag: String, message: String) { println("[$tag] WARN: $message") }
    actual fun error(tag: String, message: String, throwable: Throwable?) { System.err.println("[$tag] ERROR: $message ${throwable?.message ?: ""}") }
}
