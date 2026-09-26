package com.skillx.server.features.payments.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestorePurchaseDataSource(private val provider: FirestoreClientProvider) { fun recordPurchase(data: Map<String, Any>) = provider.getFirestore().collection("purchases").add(data).get() }
