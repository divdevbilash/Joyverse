package com.example.feature.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.UserRepository
import com.example.core.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Loading : AuthUiState
    object Unauthenticated : AuthUiState
    data class NeedsOnboarding(val uid: String) : AuthUiState
    data class Authenticated(val profile: UserProfile) : AuthUiState
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val auth = Firebase.auth

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkCurrentAuthState()
    }

    fun checkCurrentAuthState() {
        val user = auth.currentUser
        if (user == null) {
            _uiState.value = AuthUiState.Unauthenticated
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val profile = userRepository.getUserProfile(user.uid)
            if (profile != null && profile.username.isNotBlank()) {
                _uiState.value = AuthUiState.Authenticated(profile)
            } else {
                _uiState.value = AuthUiState.NeedsOnboarding(user.uid)
            }
        }
    }

    fun completeOnboarding(
        username: String,
        avatar: String,
        bio: String,
        interests: List<String>,
        favoriteCategories: List<String>,
        personalGoals: List<String>,
        preferredLanguages: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("Authentication session lost.")
            return
        }

        viewModelScope.launch {
            try {
                val newProfile = UserProfile(
                    userId = uid,
                    username = username.trim(),
                    avatar = avatar,
                    bio = bio.trim(),
                    interests = interests,
                    favoriteCategories = favoriteCategories,
                    personalGoals = personalGoals,
                    preferredLanguages = preferredLanguages,
                    recommendationPrefs = listOf("Personalized", "Exploratory"),
                    xp = 50, // Welcome XP boost
                    level = 1,
                    streak = 1
                )
                userRepository.saveUserProfile(newProfile)
                _uiState.value = AuthUiState.Authenticated(newProfile)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Failed to save profile. Please retry.")
            }
        }
    }

    fun onAuthSuccess() {
        checkCurrentAuthState()
    }
}
