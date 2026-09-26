package com.skillx.server.features.lessons.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestoreLessonDataSource(private val provider: FirestoreClientProvider) { fun getLessons(userId: String) = provider.getFirestore().collection("lessonRequests").whereEqualTo("requesterId", userId).get().get() }
