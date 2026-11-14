package com.example.core.data.repository

import android.content.Context
import com.example.R
import com.example.core.data.util.OperationType
import com.example.core.data.util.handleFirestoreError
import com.example.core.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be authenticated before accessing profile data.")
    }

    fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        val path = "users/$userId"
        val docRef = db.collection("users").document(userId)
        docRef.snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(UserProfile::class.java)
                } else {
                    null
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                handleFirestoreError(error as? Exception ?: Exception(error), OperationType.READ, path)
                emit(null)
            }
            .collect { emit(it) }
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        val docRef = db.collection("users").document(userId)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                snapshot.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.READ, docRef.path)
            null
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = hashMapOf<String, Any>(
            "userId" to uid,
            "username" to profile.username.trim(),
            "avatar" to profile.avatar,
            "bio" to profile.bio.trim(),
            "interests" to profile.interests,
            "favoriteCategories" to profile.favoriteCategories,
            "personalGoals" to profile.personalGoals,
            "preferredLanguages" to profile.preferredLanguages,
            "recommendationPrefs" to profile.recommendationPrefs,
            "xp" to profile.xp,
            "level" to profile.level,
            "streak" to profile.streak,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (profile.createdAt == null) {
            payload["createdAt"] = FieldValue.serverTimestamp()
        }

        try {
            docRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun addXpAndProgress(xpToAdd: Int) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                if (snapshot.exists()) {
                    val currentXp = (snapshot.getLong("xp") ?: 0L).toInt()
                    val newXp = currentXp + xpToAdd
                    val newLevel = calculateLevel(newXp)
                    transaction.update(
                        docRef,
                        mapOf(
                            "xp" to newXp,
                            "level" to newLevel,
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    )
                }
            }.await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
        }
    }

    private fun calculateLevel(xp: Int): Int {
        return (xp / 100).coerceAtLeast(0) + 1
    }
}
