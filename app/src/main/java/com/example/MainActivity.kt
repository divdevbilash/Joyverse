package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature.auth.AuthUiState
import com.example.feature.auth.AuthViewModel
import com.example.feature.auth.OnboardingScreen
import com.example.feature.auth.SignInScreen
import com.example.navigation.JoyverseMainScreen
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.JoyverseTheme
import com.example.ui.theme.RadiantCyan
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoyverseTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            authViewModel.checkCurrentAuthState()
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        SignInScreen(
            onAuthSuccess = {
                authViewModel.checkCurrentAuthState()
            }
        )
    } else {
        when (authUiState) {
            is AuthUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CosmicNavy),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RadiantCyan)
                }
            }
            is AuthUiState.NeedsOnboarding -> {
                OnboardingScreen(
                    onComplete = { username, avatar, bio, interests, categories, goals, languages ->
                        authViewModel.completeOnboarding(
                            username = username,
                            avatar = avatar,
                            bio = bio,
                            interests = interests,
                            favoriteCategories = categories,
                            personalGoals = goals,
                            preferredLanguages = languages,
                            onSuccess = {
                                authViewModel.checkCurrentAuthState()
                            },
                            onError = {
                                authViewModel.checkCurrentAuthState()
                            }
                        )
                    }
                )
            }
            is AuthUiState.Authenticated -> {
                val uid = currentUser!!.uid
                key(uid) {
                    JoyverseMainScreen(
                        userId = uid,
                        onSignOut = {
                            authViewModel.checkCurrentAuthState()
                        }
                    )
                }
            }
            is AuthUiState.Unauthenticated -> {
                SignInScreen(
                    onAuthSuccess = {
                        authViewModel.checkCurrentAuthState()
                    }
                )
            }
        }
    }
}
