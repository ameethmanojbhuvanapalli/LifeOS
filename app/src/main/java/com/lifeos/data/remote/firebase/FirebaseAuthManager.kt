package com.lifeos.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun currentUserId(): String? = auth.currentUser?.uid

    suspend fun ensureSignedInAnonymously(): String {
        val existing = auth.currentUser
        if (existing != null) return existing.uid

        val result = auth.signInAnonymously().await()
        return requireNotNull(result.user).uid
    }
}
