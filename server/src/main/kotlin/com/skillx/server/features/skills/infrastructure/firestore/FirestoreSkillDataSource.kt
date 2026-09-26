package com.skillx.server.features.skills.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestoreSkillDataSource(private val provider: FirestoreClientProvider) { fun getSkills(userId: String) = provider.getFirestore().collection("users").document(userId).get().get() }
