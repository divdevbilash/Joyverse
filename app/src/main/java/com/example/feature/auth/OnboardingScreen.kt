package com.example.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.components.AvatarBadge
import com.example.core.designsystem.components.AvatarPresets
import com.example.core.designsystem.components.JoyverseButton
import com.example.core.designsystem.components.JoyverseCard
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCharcoal
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.RadiantCyan
import com.example.ui.theme.StarlightMuted
import com.example.ui.theme.StarlightWhite

private val DefaultInterests = listOf(
    "Programming", "Web Development", "Android Development", "Artificial Intelligence",
    "Photography", "Travel", "Japanese Culture", "Language Learning", "Football",
    "Gaming", "Music", "Art", "Science", "Reading", "Nature", "Creative Experiments",
    "Robotics", "Astronomy", "Design", "Philosophy"
)

private val DefaultCategories = listOf(
    "Study", "Coding", "Language Learning", "Gaming", "Creativity",
    "Entertainment", "Exercise", "Personal Development", "Travel Exploration"
)

private val DefaultGoals = listOf(
    "Spend 15 mins daily on a hobby",
    "Build creative experiments",
    "Discover new countries & cultures",
    "Meet people through shared passions",
    "Learn a new programming skill",
    "Practice language vocabulary daily"
)

private val DefaultLanguages = listOf(
    "English", "Japanese", "Spanish", "French", "German", "Chinese", "Korean", "Italian"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onComplete: (
        username: String,
        avatar: String,
        bio: String,
        interests: List<String>,
        favoriteCategories: List<String>,
        personalGoals: List<String>,
        preferredLanguages: List<String>
    ) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 4

    var username by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("avatar_compass") }
    var bio by remember { mutableStateOf("") }
    var customInterestInput by remember { mutableStateOf("") }

    val selectedInterests = remember { mutableStateListOf("Programming", "Artificial Intelligence", "Gaming") }
    val selectedCategories = remember { mutableStateListOf("Coding", "Creativity", "Personal Development") }
    val selectedGoals = remember { mutableStateListOf("Spend 15 mins daily on a hobby", "Build creative experiments") }
    val selectedLanguages = remember { mutableStateListOf("English") }

    var validationError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = CosmicNavy,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "JOYVERSE ONBOARDING",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RadiantCyan,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = when (step) {
                                1 -> "Create Your Explorer Identity"
                                2 -> "Choose Your Interests"
                                3 -> "Activity Goals & Categories"
                                else -> "Languages & Preferences"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = StarlightWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    if (step > 1) {
                        IconButton(onClick = { step -= 1 }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = StarlightWhite
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CosmicNavy
                )
            )
        },
        bottomBar = {
            Surface(
                color = CosmicCharcoal,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = CosmicBorder, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step $step of $totalSteps",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = StarlightMuted,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    JoyverseButton(
                        text = if (step == totalSteps) "Enter Universe" else "Continue",
                        icon = if (step == totalSteps) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                        testTag = "onboarding_continue_button",
                        onClick = {
                            when (step) {
                                1 -> {
                                    if (username.trim().length < 3) {
                                        validationError = "Username must be at least 3 characters."
                                    } else {
                                        validationError = null
                                        step = 2
                                    }
                                }
                                2 -> {
                                    if (selectedInterests.isEmpty()) {
                                        validationError = "Please select at least 1 interest."
                                    } else {
                                        validationError = null
                                        step = 3
                                    }
                                }
                                3 -> {
                                    step = 4
                                }
                                4 -> {
                                    onComplete(
                                        username.trim(),
                                        selectedAvatar,
                                        bio.trim(),
                                        selectedInterests.toList(),
                                        selectedCategories.toList(),
                                        selectedGoals.toList(),
                                        selectedLanguages.toList()
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Step Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..totalSteps) {
                    val barModifier = if (i <= step) {
                        Modifier.background(Brush.horizontalGradient(listOf(ElectricPurple, RadiantCyan)))
                    } else {
                        Modifier.background(Color(0xFF1E293B))
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .then(barModifier)
                    )
                }
            }

            if (validationError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = validationError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            AnimatedContent(targetState = step, label = "onboarding_step") { currentStep ->
                when (currentStep) {
                    1 -> {
                        Step1Identity(
                            username = username,
                            onUsernameChange = { username = it; validationError = null },
                            selectedAvatar = selectedAvatar,
                            onAvatarSelect = { selectedAvatar = it },
                            bio = bio,
                            onBioChange = { bio = it }
                        )
                    }
                    2 -> {
                        Step2Interests(
                            selectedInterests = selectedInterests,
                            onToggleInterest = { tag ->
                                if (selectedInterests.contains(tag)) selectedInterests.remove(tag)
                                else selectedInterests.add(tag)
                            },
                            customInput = customInterestInput,
                            onCustomInputChange = { customInterestInput = it },
                            onAddCustom = {
                                if (customInterestInput.isNotBlank() && !selectedInterests.contains(customInterestInput.trim())) {
                                    selectedInterests.add(customInterestInput.trim())
                                    customInterestInput = ""
                                }
                            }
                        )
                    }
                    3 -> {
                        Step3CategoriesAndGoals(
                            selectedCategories = selectedCategories,
                            onToggleCategory = { cat ->
                                if (selectedCategories.contains(cat)) selectedCategories.remove(cat)
                                else selectedCategories.add(cat)
                            },
                            selectedGoals = selectedGoals,
                            onToggleGoal = { goal ->
                                if (selectedGoals.contains(goal)) selectedGoals.remove(goal)
                                else selectedGoals.add(goal)
                            }
                        )
                    }
                    4 -> {
                        Step4Languages(
                            selectedLanguages = selectedLanguages,
                            onToggleLanguage = { lang ->
                                if (selectedLanguages.contains(lang)) {
                                    if (selectedLanguages.size > 1) selectedLanguages.remove(lang)
                                } else {
                                    selectedLanguages.add(lang)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Step1Identity(
    username: String,
    onUsernameChange: (String) -> Unit,
    selectedAvatar: String,
    onAvatarSelect: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Your identity in JOYVERSE is completely pseudonymous. No real name or email is shown to others.",
            style = MaterialTheme.typography.bodySmall.copy(color = StarlightMuted)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SELECT YOUR AVATAR",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AvatarPresets.take(4).forEach { preset ->
                AvatarPresetItem(
                    preset = preset,
                    isSelected = preset.id == selectedAvatar,
                    onClick = { onAvatarSelect(preset.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AvatarPresets.drop(4).take(4).forEach { preset ->
                AvatarPresetItem(
                    preset = preset,
                    isSelected = preset.id == selectedAvatar,
                    onClick = { onAvatarSelect(preset.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "EXPLORER USERNAME",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            placeholder = { Text("e.g. starlight_coder, astro_builder", color = StarlightMuted) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_username_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RadiantCyan,
                unfocusedBorderColor = CosmicBorder,
                focusedTextColor = StarlightWhite,
                unfocusedTextColor = StarlightWhite
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "EXPLORER BIO (OPTIONAL)",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = onBioChange,
            placeholder = { Text("What drives your curiosity?", color = StarlightMuted) },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RadiantCyan,
                unfocusedBorderColor = CosmicBorder,
                focusedTextColor = StarlightWhite,
                unfocusedTextColor = StarlightWhite
            ),
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
private fun AvatarPresetItem(
    preset: com.example.core.designsystem.components.AvatarPreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) ElectricPurple.copy(alpha = 0.3f) else CosmicCard
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) RadiantCyan else CosmicBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = preset.emoji, fontSize = 28.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step2Interests(
    selectedInterests: List<String>,
    onToggleInterest: (String) -> Unit,
    customInput: String,
    onCustomInputChange: (String) -> Unit,
    onAddCustom: () -> Unit
) {
    Column {
        Text(
            text = "Select what you love. JOYVERSE connects you with projects, challenges, and hobbyists matching your exact passions.",
            style = MaterialTheme.typography.bodySmall.copy(color = StarlightMuted)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Interest Input
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = customInput,
                onValueChange = onCustomInputChange,
                placeholder = { Text("Add custom hobby...", color = StarlightMuted) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantCyan,
                    unfocusedBorderColor = CosmicBorder,
                    focusedTextColor = StarlightWhite,
                    unfocusedTextColor = StarlightWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onAddCustom,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ElectricPurple)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add custom interest", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val allInterests = (DefaultInterests + selectedInterests).distinct()
            allInterests.forEach { interest ->
                val isSelected = selectedInterests.contains(interest)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleInterest(interest) },
                    label = {
                        Text(
                            text = interest,
                            color = if (isSelected) StarlightWhite else StarlightMuted,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CosmicCard,
                        selectedContainerColor = ElectricPurple
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = CosmicBorder,
                        selectedBorderColor = RadiantCyan
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step3CategoriesAndGoals(
    selectedCategories: List<String>,
    onToggleCategory: (String) -> Unit,
    selectedGoals: List<String>,
    onToggleGoal: (String) -> Unit
) {
    Column {
        Text(
            text = "FAVORITE ACTIVITY CATEGORIES",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultCategories.forEach { category ->
                val isSelected = selectedCategories.contains(category)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleCategory(category) },
                    label = {
                        Text(
                            text = category,
                            color = if (isSelected) StarlightWhite else StarlightMuted,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CosmicCard,
                        selectedContainerColor = RadiantCyan.copy(alpha = 0.8f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = CosmicBorder,
                        selectedBorderColor = RadiantCyan
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "PERSONAL ACTIVITY GOALS",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        DefaultGoals.forEach { goal ->
            val isSelected = selectedGoals.contains(goal)
            JoyverseCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onToggleGoal(goal) },
                backgroundColor = if (isSelected) ElectricPurple.copy(alpha = 0.2f) else CosmicCard,
                borderColor = if (isSelected) RadiantCyan else CosmicBorder
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, if (isSelected) RadiantCyan else StarlightMuted, CircleShape)
                            .background(if (isSelected) RadiantCyan else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = goal,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) StarlightWhite else StarlightMuted,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step4Languages(
    selectedLanguages: List<String>,
    onToggleLanguage: (String) -> Unit
) {
    Column {
        Text(
            text = "PREFERRED LANGUAGES FOR DISCOVERY",
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Used for FreeTime vocabulary lessons, WorldVerse cultural guides, and global challenges.",
            style = MaterialTheme.typography.bodySmall.copy(color = StarlightMuted)
        )
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultLanguages.forEach { lang ->
                val isSelected = selectedLanguages.contains(lang)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleLanguage(lang) },
                    label = {
                        Text(
                            text = lang,
                            color = if (isSelected) StarlightWhite else StarlightMuted
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CosmicCard,
                        selectedContainerColor = ElectricPurple
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        JoyverseCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0x66161F33),
            borderColor = CosmicGold.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎁", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "+50 XP Welcome Explorer Bonus",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CosmicGold
                        )
                    )
                    Text(
                        text = "Ready to launch your journey in JOYVERSE!",
                        style = MaterialTheme.typography.bodySmall.copy(color = StarlightMuted)
                    )
                }
            }
        }
    }
}
