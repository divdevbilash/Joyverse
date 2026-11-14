package com.example.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserActivity(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "Personal Development",
    val icon: String = "compass",
    val color: String = "#8B5CF6",
    val priority: String = "Medium",
    val estimatedMinutes: Int = 15,
    val scheduledDate: String = "",
    val deadline: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
