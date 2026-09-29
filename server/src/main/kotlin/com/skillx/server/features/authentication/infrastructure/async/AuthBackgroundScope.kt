package com.skillx.server.features.authentication.infrastructure.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** Server-lifetime scope for auth work that must not delay (or be timed by) the HTTP response. */
class AuthBackgroundScope : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO)
