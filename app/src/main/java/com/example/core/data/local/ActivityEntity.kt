package com.example.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.UserActivity

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val category: String,
    val icon: String,
    val color: String,
    val priority: String,
    val estimatedMinutes: Int,
    val scheduledDate: String,
    val deadline: String,
    val notes: String,
    val isCompleted: Boolean,
    val createdAtEpoch: Long,
    val updatedAtEpoch: Long,
    val isSynced: Boolean = false
) {
    fun toDomainModel(): UserActivity {
        return UserActivity(
            id = id,
            userId = userId,
            title = title,
            description = description,
            category = category,
            icon = icon,
            color = color,
            priority = priority,
            estimatedMinutes = estimatedMinutes,
            scheduledDate = scheduledDate,
            deadline = deadline,
            notes = notes,
            isCompleted = isCompleted
        )
    }

    companion object {
        fun fromDomain(model: UserActivity, isSynced: Boolean = false): ActivityEntity {
            return ActivityEntity(
                id = model.id.ifBlank { java.util.UUID.randomUUID().toString() },
                userId = model.userId,
                title = model.title,
                description = model.description,
                category = model.category,
                icon = model.icon,
                color = model.color,
                priority = model.priority,
                estimatedMinutes = model.estimatedMinutes,
                scheduledDate = model.scheduledDate,
                deadline = model.deadline,
                notes = model.notes,
                isCompleted = model.isCompleted,
                createdAtEpoch = model.createdAt?.toDate()?.time ?: System.currentTimeMillis(),
                updatedAtEpoch = model.updatedAt?.toDate()?.time ?: System.currentTimeMillis(),
                isSynced = isSynced
            )
        }
    }
}
