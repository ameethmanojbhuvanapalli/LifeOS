package com.lifeos.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    val firestore: FirebaseFirestore
) {
    fun todosCollection(userId: String) = firestore.collection("users").document(userId).collection("todos")
    fun financeCollection(userId: String) = firestore.collection("users").document(userId).collection("finance")
    fun trackersCollection(userId: String) = firestore.collection("users").document(userId).collection("trackers")
}
