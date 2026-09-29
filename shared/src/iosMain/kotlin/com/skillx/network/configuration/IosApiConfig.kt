package com.skillx.network.configuration

import platform.Foundation.NSBundle

/**
 * Server base URL, read from Info.plist — mirrors androidApp's API_BASE_URL BuildConfig field.
 */
internal object IosApiConfig {
    val baseUrl: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("API_BASE_URL") as? String ?: ""
}
