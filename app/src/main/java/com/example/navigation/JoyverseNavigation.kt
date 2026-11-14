package com.example.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature.activities.ActivitiesScreen
import com.example.feature.explore.ExploreScreen
import com.example.feature.freetime.FreeTimeCornerScreen
import com.example.feature.home.HomeScreen
import com.example.feature.home.HomeViewModel
import com.example.feature.profile.ProfileScreen
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCharcoal
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.RadiantCyan
import com.example.ui.theme.StarlightMuted
import com.example.ui.theme.StarlightWhite

enum class JoyverseDestination(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_explore"),
    ACTIVITIES("Activities", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle, "nav_activities"),
    FREETIME("FreeTime", Icons.Filled.HourglassTop, Icons.Outlined.HourglassTop, "nav_freetime"),
    PROFILE("Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, "nav_profile")
}

@Composable
fun JoyverseMainScreen(
    userId: String,
    onSignOut: () -> Unit
) {
    var currentDestination by remember { mutableStateOf(JoyverseDestination.HOME) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(key = userId, factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            val app = context.applicationContext as android.app.Application
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(app, userId) as T
        }
    })

    // BackHandler to navigate back to Home if in another screen
    BackHandler(enabled = currentDestination != JoyverseDestination.HOME) {
        currentDestination = JoyverseDestination.HOME
    }

    Scaffold(
        containerColor = CosmicNavy,
        bottomBar = {
            NavigationBar(
                containerColor = CosmicCharcoal,
                modifier = Modifier
                    .navigationBarsPadding()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .border(width = 1.dp, color = CosmicBorder, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                JoyverseDestination.values().forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = destination.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = RadiantCyan,
                            unselectedIconColor = StarlightMuted,
                            unselectedTextColor = StarlightMuted,
                            indicatorColor = ElectricPurple
                        ),
                        modifier = Modifier.testTag(destination.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
                label = "navigation_crossfade"
            ) { dest ->
                when (dest) {
                    JoyverseDestination.HOME -> {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToFreeTime = { currentDestination = JoyverseDestination.FREETIME },
                            onNavigateToExplore = { currentDestination = JoyverseDestination.EXPLORE },
                            onNavigateToActivities = { currentDestination = JoyverseDestination.ACTIVITIES },
                            onNavigateToProfile = { currentDestination = JoyverseDestination.PROFILE }
                        )
                    }
                    JoyverseDestination.EXPLORE -> {
                        ExploreScreen()
                    }
                    JoyverseDestination.ACTIVITIES -> {
                        ActivitiesScreen(userId = userId)
                    }
                    JoyverseDestination.FREETIME -> {
                        FreeTimeCornerScreen(
                            onAddActivity = { title, category, minutes ->
                                homeViewModel.addQuickActivity(title, category, minutes)
                            }
                        )
                    }
                    JoyverseDestination.PROFILE -> {
                        ProfileScreen(
                            userId = userId,
                            onSignOut = onSignOut
                        )
                    }
                }
            }
        }
    }
}
