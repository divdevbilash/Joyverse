package com.example.feature.freetime

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.core.designsystem.components.JoyverseButton
import com.example.core.designsystem.components.JoyverseCard
import com.example.core.model.FreeTimeSuggestion
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCharcoal
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.RadiantCyan
import com.example.ui.theme.StarlightMuted
import com.example.ui.theme.StarlightWhite
import kotlinx.coroutines.launch

private val CuratedSuggestions = listOf(
    FreeTimeSuggestion(
        id = "ft_jp_vocab",
        title = "Learn 3 Japanese Culture Words",
        description = "Discover 'Ikigai' (life purpose), 'Wabi-sabi' (beauty in imperfection), and 'Yūgen' (subtle grace).",
        durationMinutes = 5,
        category = "Language Learning",
        mood = "Curious",
        energy = "Low",
        rationale = "Quick 5-minute cultural burst that expands your worldview without mental strain.",
        icon = "🎌",
        color = "#EC4899"
    ),
    FreeTimeSuggestion(
        id = "ft_css_trick",
        title = "Try a Modern CSS Grid Glassmorphism Effect",
        description = "Experiment with backdrop-filter: blur(12px) and gradient borders in a quick sandbox snippet.",
        durationMinutes = 15,
        category = "Coding",
        mood = "Creative",
        energy = "Medium",
        rationale = "Inspires your creative design eye in just 15 minutes of hands-on coding.",
        icon = "🎨",
        color = "#8B5CF6"
    ),
    FreeTimeSuggestion(
        id = "ft_micro_challenge",
        title = "Mindful Breath & Nature Observation",
        description = "Step away from screens for 5 minutes, observe sky cloud patterns, and reset focus.",
        durationMinutes = 5,
        category = "Personal Development",
        mood = "Relaxed",
        energy = "Low",
        rationale = "Perfect low-energy restorative reset between tasks.",
        icon = "🍃",
        color = "#10B981"
    ),
    FreeTimeSuggestion(
        id = "ft_code_experiment",
        title = "Build a Mini Kotlin Coroutine Flow Experiment",
        description = "Create a custom state flow transformer with debounce and distinctUntilChanged.",
        durationMinutes = 30,
        category = "Coding",
        mood = "Motivated",
        energy = "High",
        rationale = "Deep dive into reactive architecture for motivated problem-solving sessions.",
        icon = "⚡",
        color = "#06B6D4"
    ),
    FreeTimeSuggestion(
        id = "ft_world_doc",
        title = "Explore Ancient Kyoto Architecture",
        description = "Discover how wooden pagodas survived seismic shocks for over a millennium using Shinbashira design.",
        durationMinutes = 15,
        category = "Travel Exploration",
        mood = "Curious",
        energy = "Medium",
        rationale = "Fascinating blend of architectural engineering and historical culture.",
        icon = "🏯",
        color = "#F59E0B"
    ),
    FreeTimeSuggestion(
        id = "ft_photo_challenge",
        title = "Household Macro Photography Quest",
        description = "Capture 3 ordinary textures (fabric weave, water droplet, coin edge) from dramatic angles.",
        durationMinutes = 15,
        category = "Creativity",
        mood = "Creative",
        energy = "Medium",
        rationale = "Encourages noticing extraordinary details in ordinary surroundings.",
        icon = "📷",
        color = "#A855F7"
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FreeTimeCornerScreen(
    onAddActivity: (title: String, category: String, minutes: Int) -> Unit
) {
    var selectedMinutes by remember { mutableIntStateOf(15) }
    var selectedMood by remember { mutableStateOf("Curious") }
    var selectedEnergy by remember { mutableStateOf("Medium") }
    var suggestionIndex by remember { mutableIntStateOf(0) }
    var isAdded by remember { mutableStateOf(false) }

    val times = listOf(5, 10, 15, 30, 45, 60)
    val moods = listOf("Curious", "Relaxed", "Creative", "Motivated", "Adventurous")
    val energies = listOf("Low", "Medium", "High")

    // Dynamic suggestion based on selections
    val currentSuggestion = CuratedSuggestions[suggestionIndex % CuratedSuggestions.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicNavy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = ElectricPurple.copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⏱️", fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "FREETIME CORNER",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StarlightWhite
                        )
                    )
                    Text(
                        text = "Intelligent free-time discovery based on your mood & minutes",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = RadiantCyan,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Time Selection
            Text(
                text = "AVAILABLE TIME",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ElectricPurple,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                times.forEach { mins ->
                    val isSelected = selectedMinutes == mins
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedMinutes = mins
                            suggestionIndex = (suggestionIndex + 1) % CuratedSuggestions.size
                            isAdded = false
                        },
                        label = { Text("$mins mins") },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CosmicCard,
                            selectedContainerColor = ElectricPurple
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Current Mood
            Text(
                text = "CURRENT MOOD",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = RadiantCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moods.forEach { mood ->
                    val isSelected = selectedMood == mood
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedMood = mood
                            suggestionIndex = (suggestionIndex + 1) % CuratedSuggestions.size
                            isAdded = false
                        },
                        label = { Text(mood) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CosmicCard,
                            selectedContainerColor = RadiantCyan.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Energy Level
            Text(
                text = "ENERGY LEVEL",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CosmicGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                energies.forEach { energy ->
                    val isSelected = selectedEnergy == energy
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedEnergy = energy
                            suggestionIndex = (suggestionIndex + 1) % CuratedSuggestions.size
                            isAdded = false
                        },
                        label = { Text(energy) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CosmicCard,
                            selectedContainerColor = CosmicGold
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Recommended Activity Card
            JoyverseCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = CosmicCharcoal,
                borderColor = RadiantCyan.copy(alpha = 0.6f)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RadiantCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "RECOMMENDED FOR YOU",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RadiantCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "${currentSuggestion.durationMinutes} mins",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = StarlightMuted,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = currentSuggestion.icon, fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = currentSuggestion.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightWhite
                                )
                            )
                            Text(
                                text = currentSuggestion.category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricPurple
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = currentSuggestion.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = StarlightWhite,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Rationale
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CosmicCard,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 ${currentSuggestion.rationale}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StarlightMuted,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Surprise Me / Another button
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CosmicCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    suggestionIndex = (suggestionIndex + 1) % CuratedSuggestions.size
                                    isAdded = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = StarlightWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Another",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = StarlightWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        // Add to My Activities
                        JoyverseButton(
                            text = if (isAdded) "Added ✓" else "Add to Activities",
                            icon = if (isAdded) Icons.Default.Check else Icons.Default.Add,
                            enabled = !isAdded,
                            modifier = Modifier.weight(1.3f),
                            testTag = "freetime_add_activity_button",
                            onClick = {
                                onAddActivity(
                                    currentSuggestion.title,
                                    currentSuggestion.category,
                                    currentSuggestion.durationMinutes
                                )
                                isAdded = true
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
