package com.skillx.server.features.ratings.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestoreRatingDataSource(private val provider: FirestoreClientProvider) { fun getRatings(userId: String) = provider.getFirestore().collection("ratings").whereEqualTo("ratedUserId", userId).get().get() }
