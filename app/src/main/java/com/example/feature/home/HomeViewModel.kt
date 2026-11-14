package com.example.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.ActivityRepository
import com.example.core.data.repository.UserRepository
import com.example.core.model.DailyQuest
import com.example.core.model.UserActivity
import com.example.core.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class HomeUiState(
    val profile: UserProfile? = null,
    val activities: List<UserActivity> = emptyList(),
    val dailyQuests: List<DailyQuest> = emptyList(),
    val greeting: String = "Welcome Explorer",
    val todayCompletionRate: Float = 0f,
    val isLoading: Boolean = false
)

class HomeViewModel(
    application: Application,
    private val userId: String
) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val activityRepository = ActivityRepository(application)

    private val _quests = MutableStateFlow(
        listOf(
            DailyQuest(
                id = "quest_activity_1",
                title = "Launch Your Spark",
                description = "Complete 1 personal activity today",
                xpReward = 30,
                targetCount = 1,
                currentCount = 0,
                isCompleted = false,
                category = "Activities",
                icon = "⚡"
            ),
            DailyQuest(
                id = "quest_freetime_1",
                title = "FreeTime Discovery",
                description = "Take a 5-minute FreeTime break",
                xpReward = 20,
                targetCount = 1,
                currentCount = 0,
                isCompleted = false,
                category = "FreeTime",
                icon = "⏱️"
            ),
            DailyQuest(
                id = "quest_world_1",
                title = "Global Explorer",
                description = "Discover a cultural fact in WorldVerse",
                xpReward = 25,
                targetCount = 1,
                currentCount = 0,
                isCompleted = false,
                category = "WorldVerse",
                icon = "🌍"
            )
        )
    )

    val uiState: StateFlow<HomeUiState> = combine(
        userRepository.observeUserProfile(userId),
        activityRepository.observeActivities(userId),
        _quests
    ) { profile, activities, quests ->
        val completedCount = activities.count { it.isCompleted }
        val rate = if (activities.isNotEmpty()) completedCount.toFloat() / activities.size else 0f

        val updatedQuests = quests.map { quest ->
            if (quest.id == "quest_activity_1") {
                val done = completedCount >= 1
                quest.copy(
                    currentCount = completedCount.coerceAtMost(1),
                    isCompleted = done
                )
            } else {
                quest
            }
        }

        HomeUiState(
            profile = profile,
            activities = activities,
            dailyQuests = updatedQuests,
            greeting = generateTimeGreeting(profile?.username),
            todayCompletionRate = rate,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun toggleActivity(activity: UserActivity) {
        viewModelScope.launch {
            activityRepository.toggleActivityCompletion(activity)
            if (!activity.isCompleted) {
                // Award 15 XP for completing an activity
                userRepository.addXpAndProgress(15)
            }
        }
    }

    fun completeQuest(questId: String) {
        val quest = _quests.value.find { it.id == questId } ?: return
        if (quest.isCompleted) return

        viewModelScope.launch {
            userRepository.addXpAndProgress(quest.xpReward)
            _quests.value = _quests.value.map {
                if (it.id == questId) it.copy(isCompleted = true, currentCount = it.targetCount) else it
            }
        }
    }

    fun addQuickActivity(title: String, category: String, minutes: Int) {
        viewModelScope.launch {
            val newActivity = UserActivity(
                userId = userId,
                title = title,
                category = category,
                estimatedMinutes = minutes,
                isCompleted = false
            )
            activityRepository.createActivity(newActivity)
        }
    }

    private fun generateTimeGreeting(username: String?): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeWord = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..21 -> "Good evening"
            else -> "Night owl hours"
        }
        val name = if (!username.isNullOrBlank()) "@$username" else "Explorer"
        return "$timeWord, $name"
    }
}
