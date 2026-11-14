package com.example.core.data.repository

import android.content.Context
import com.example.R
import com.example.core.data.local.ActivityDao
import com.example.core.data.local.ActivityEntity
import com.example.core.data.local.JoyverseDatabase
import com.example.core.data.util.OperationType
import com.example.core.data.util.handleFirestoreError
import com.example.core.model.UserActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class ActivityRepository(
    private val activityDao: ActivityDao,
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        JoyverseDatabase.getDatabase(context).activityDao(),
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth
    private val scope = CoroutineScope(Dispatchers.IO)

    fun observeActivities(userId: String): Flow<List<UserActivity>> {
        return activityDao.observeActivities(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun createActivity(activity: UserActivity): String {
        val uid = auth.currentUser?.uid ?: activity.userId
        val activityId = if (activity.id.isNotBlank()) activity.id else UUID.randomUUID().toString()
        val activityWithId = activity.copy(id = activityId, userId = uid)

        // 1. Save to local Room database immediately (Offline-first)
        val entity = ActivityEntity.fromDomain(activityWithId, isSynced = false)
        activityDao.insertOrUpdate(entity)

        // 2. Sync to Firestore in background
        if (auth.currentUser != null) {
            scope.launch {
                syncActivityToCloud(activityWithId)
            }
        }
        return activityId
    }

    suspend fun toggleActivityCompletion(activity: UserActivity) {
        val updated = activity.copy(isCompleted = !activity.isCompleted)
        val entity = ActivityEntity.fromDomain(updated, isSynced = false)
        activityDao.insertOrUpdate(entity)

        if (auth.currentUser != null) {
            scope.launch {
                syncActivityToCloud(updated)
            }
        }
    }

    suspend fun deleteActivity(userId: String, activityId: String) {
        activityDao.deleteById(userId, activityId)

        if (auth.currentUser != null) {
            val docRef = db.collection("users").document(userId).collection("activities").document(activityId)
            try {
                docRef.delete().await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.DELETE, docRef.path)
            }
        }
    }

    private suspend fun syncActivityToCloud(activity: UserActivity) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid).collection("activities").document(activity.id)
        val payload = hashMapOf<String, Any>(
            "userId" to uid,
            "title" to activity.title,
            "description" to activity.description,
            "category" to activity.category,
            "icon" to activity.icon,
            "color" to activity.color,
            "priority" to activity.priority,
            "estimatedMinutes" to activity.estimatedMinutes,
            "scheduledDate" to activity.scheduledDate,
            "deadline" to activity.deadline,
            "notes" to activity.notes,
            "isCompleted" to activity.isCompleted,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (activity.createdAt == null) {
            payload["createdAt"] = FieldValue.serverTimestamp()
        }

        try {
            docRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
            activityDao.markSynced(activity.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
        }
    }
}
