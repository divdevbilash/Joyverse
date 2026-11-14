package com.example.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val avatar: String = "avatar_portal",
    val bio: String = "",
    val interests: List<String> = emptyList(),
    val favoriteCategories: List<String> = emptyList(),
    val personalGoals: List<String> = emptyList(),
    val preferredLanguages: List<String> = listOf("English"),
    val recommendationPrefs: List<String> = emptyList(),
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 1,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
