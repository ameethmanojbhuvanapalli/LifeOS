package com.lifeos.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun currentUserId(): String? = auth.currentUser?.uid
}
