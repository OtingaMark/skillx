package com.skillx.core.time

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object Clock {
    actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}
