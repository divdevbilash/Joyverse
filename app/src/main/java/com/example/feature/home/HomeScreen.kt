package com.example.feature.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.core.designsystem.components.JoyverseCard
import com.example.core.designsystem.components.XpProgressBar
import com.example.core.model.DailyQuest
import com.example.core.model.UserActivity
import com.example.ui.theme.AstralBlue
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToFreeTime: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToActivities: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showQuickAddDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicNavy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Bar with Greeting, Avatar and Streak Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.greeting,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StarlightWhite,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Personal Universe",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RadiantCyan,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CosmicCharcoal,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGold.copy(alpha = 0.5f)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = CosmicGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${uiState.profile?.streak ?: 1}d",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightWhite
                                )
                            )
                        }
                    }

                    // Avatar
                    AvatarBadge(
                        avatarId = uiState.profile?.avatar ?: "avatar_compass",
                        size = 46.dp,
                        onClick = onNavigateToProfile
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progression & Level Card
            JoyverseCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = CosmicCharcoal,
                borderColor = CosmicBorder
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    XpProgressBar(
                        xp = uiState.profile?.xp ?: 50,
                        level = uiState.profile?.level ?: 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interactive Dual Quick Action Cards (FreeTime Corner & WorldVerse)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // FreeTime Corner Card
                JoyverseCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onNavigateToFreeTime),
                    backgroundColor = Color(0x66182238),
                    borderColor = ElectricPurple.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ElectricPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = ElectricPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "FreeTime Corner",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StarlightWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Have 5-30 mins? Get instant ideas",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StarlightMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // WorldVerse Card
                JoyverseCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onNavigateToExplore),
                    backgroundColor = Color(0x66182238),
                    borderColor = RadiantCyan.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RadiantCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = RadiantCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "WorldVerse",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StarlightWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Explore cultures & global facts",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StarlightMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Quests Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY QUESTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = RadiantCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Earn XP",
                    style = MaterialTheme.typography.labelSmall.copy(color = StarlightMuted)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            uiState.dailyQuests.forEach { quest ->
                DailyQuestItem(
                    quest = quest,
                    onClaim = { viewModel.completeQuest(quest.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Activities Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UPCOMING ACTIVITIES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricPurple,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showQuickAddDialog = true },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("quick_add_activity_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Activity",
                            tint = RadiantCyan
                        )
                    }
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = RadiantCyan,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .clickable(onClick = onNavigateToActivities)
                            .padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.activities.isEmpty()) {
                JoyverseCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CosmicCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "✨", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Activities Scheduled Yet",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StarlightWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Add your first hobby, study, or creative goal to track your universe.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StarlightMuted
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showQuickAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Create First Activity")
                        }
                    }
                }
            } else {
                uiState.activities.take(4).forEach { activity ->
                    ActivityHomeItem(
                        activity = activity,
                        onToggle = { viewModel.toggleActivity(activity) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interests Explorer Sparks
            if (!uiState.profile?.interests.isNullOrEmpty()) {
                Text(
                    text = "YOUR ACTIVE PASSIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CosmicGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.profile!!.interests.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CosmicCharcoal,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                        ) {
                            Text(
                                text = "# $tag",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StarlightWhite,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Quick Add Activity Dialog
        if (showQuickAddDialog) {
            QuickAddActivityDialog(
                onDismiss = { showQuickAddDialog = false },
                onAdd = { title, category, minutes ->
                    viewModel.addQuickActivity(title, category, minutes)
                    showQuickAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun DailyQuestItem(
    quest: DailyQuest,
    onClaim: () -> Unit
) {
    JoyverseCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (quest.isCompleted) Color(0x3310B981) else CosmicCard,
        borderColor = if (quest.isCompleted) NeonEmerald.copy(alpha = 0.5f) else CosmicBorder
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = quest.icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quest.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StarlightWhite
                    )
                )
                Text(
                    text = quest.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            if (quest.isCompleted) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NeonEmerald.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${quest.xpReward} XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ElectricPurple.copy(alpha = 0.2f),
                    modifier = Modifier.clickable(onClick = onClaim)
                ) {
                    Text(
                        text = "+${quest.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RadiantCyan,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityHomeItem(
    activity: UserActivity,
    onToggle: () -> Unit
) {
    JoyverseCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        backgroundColor = if (activity.isCompleted) Color(0x44131B2E) else CosmicCard,
        borderColor = if (activity.isCompleted) NeonEmerald.copy(alpha = 0.3f) else CosmicBorder
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (activity.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle completion",
                    tint = if (activity.isCompleted) NeonEmerald else StarlightMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (activity.isCompleted) StarlightMuted else StarlightWhite
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activity.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricPurple,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = " • ${activity.estimatedMinutes}m",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StarlightMuted
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAddActivityDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, category: String, minutes: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Coding") }
    var minutes by remember { mutableStateOf("15") }

    val categories = listOf("Coding", "Study", "Language Learning", "Creativity", "Gaming", "Exercise")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CosmicCharcoal,
        title = {
            Text(text = "Add New Activity", color = StarlightWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Activity Title", color = StarlightMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantCyan,
                        unfocusedBorderColor = CosmicBorder,
                        focusedTextColor = StarlightWhite,
                        unfocusedTextColor = StarlightWhite
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Category", style = MaterialTheme.typography.labelSmall.copy(color = RadiantCyan))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (category == cat) ElectricPurple else CosmicCard,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = cat }
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StarlightWhite,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Duration (minutes)", color = StarlightMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantCyan,
                        unfocusedBorderColor = CosmicBorder,
                        focusedTextColor = StarlightWhite,
                        unfocusedTextColor = StarlightWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title.trim(), category, minutes.toIntOrNull() ?: 15)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                enabled = title.isNotBlank()
            ) {
                Text("Add Activity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarlightMuted)
            }
        }
    )
}
