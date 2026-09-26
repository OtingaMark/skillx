package com.skillx.core.time

actual object Clock {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}
