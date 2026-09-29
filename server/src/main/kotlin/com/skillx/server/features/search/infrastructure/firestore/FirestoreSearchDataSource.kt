package com.skillx.server.features.search.infrastructure.firestore

import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.repository.SearchRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.await

/**
 * Reads only the public fields needed for discovery (projection keeps email, password
 * hash and the rest of the profile out of the query result entirely).
 */
class FirestoreSearchDataSource(
    private val provider: FirestoreClientProvider
) : SearchRepository {

    override suspend fun loadPeople(): List<PersonSummary> {
        val snapshot = provider.getFirestore()
            .collection("users")
            .select("name", "teachSkills", "learnSkills")
            .get()
            .await()

        return snapshot.documents.map { doc ->
            PersonSummary(
                uid = doc.id,
                name = doc.getString("name").orEmpty(),
                teachSkills = stringList(doc.get("teachSkills")),
                learnSkills = stringList(doc.get("learnSkills"))
            )
        }
    }

    private fun stringList(value: Any?): List<String> =
        (value as? List<*>)?.filterIsInstance<String>().orEmpty()
}
