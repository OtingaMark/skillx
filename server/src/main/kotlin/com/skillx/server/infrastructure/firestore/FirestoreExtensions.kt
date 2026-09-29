package com.skillx.server.infrastructure.firestore

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureToListenableFuture
import kotlinx.coroutines.guava.await

/**
 * Converts a Firestore ApiFuture into a Kotlin suspend function.
 * ApiFuture isn't itself a Guava ListenableFuture, so it's bridged with
 * ApiFutureToListenableFuture before delegating to kotlinx-coroutines-guava's await().
 */
suspend fun <T> ApiFuture<T>.await(): T = ApiFutureToListenableFuture(this).await()
