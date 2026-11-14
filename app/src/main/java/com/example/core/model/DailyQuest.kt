package com.example.core.model

data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val targetCount: Int,
    val currentCount: Int,
    val isCompleted: Boolean,
    val category: String,
    val icon: String
)

data class FreeTimeSuggestion(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val category: String,
    val mood: String,
    val energy: String,
    val rationale: String,
    val icon: String,
    val color: String
)
