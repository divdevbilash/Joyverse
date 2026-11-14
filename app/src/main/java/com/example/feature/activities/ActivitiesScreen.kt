package com.example.feature.activities

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.repository.ActivityRepository
import com.example.core.designsystem.components.JoyverseCard
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
import kotlinx.coroutines.launch

private val AllCategories = listOf(
    "All", "Study", "Coding", "Language Learning", "Gaming",
    "Creativity", "Entertainment", "Exercise", "Personal Development", "Travel Exploration"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActivitiesScreen(
    userId: String
) {
    val context = LocalContext.current
    val repository = remember { ActivityRepository(context) }
    val scope = rememberCoroutineScope()

    val activities by repository.observeActivities(userId).collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }

    val filteredActivities = activities.filter { activity ->
        val matchesCategory = if (selectedFilter == "All") true else activity.category == selectedFilter
        val matchesSearch = if (searchQuery.isBlank()) true else activity.title.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val totalCount = activities.size
    val completedCount = activities.count { it.isCompleted }

    Scaffold(
        containerColor = CosmicNavy,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = ElectricPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_activity")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Activity")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACTIVITY UNIVERSE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StarlightWhite
                        )
                    )
                    Text(
                        text = "$completedCount of $totalCount completed (Offline-Synced)",
                        style = MaterialTheme.typography.bodySmall.copy(color = RadiantCyan)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search activities...", color = StarlightMuted) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = StarlightMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantCyan,
                    unfocusedBorderColor = CosmicBorder,
                    focusedTextColor = StarlightWhite,
                    unfocusedTextColor = StarlightWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AllCategories.take(6).forEach { category ->
                    val isSelected = selectedFilter == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = category },
                        label = { Text(category, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CosmicCard,
                            selectedContainerColor = ElectricPurple
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredActivities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🌌", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Activities Found",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StarlightWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap the + button to launch an activity in this category.",
                            style = MaterialTheme.typography.bodySmall.copy(color = StarlightMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredActivities, key = { it.id }) { activity ->
                        ActivityRowItem(
                            activity = activity,
                            onToggle = {
                                scope.launch { repository.toggleActivityCompletion(activity) }
                            },
                            onDelete = {
                                scope.launch { repository.deleteActivity(userId, activity.id) }
                            }
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateActivityDialog(
                onDismiss = { showCreateDialog = false },
                onSave = { newActivity ->
                    scope.launch {
                        repository.createActivity(newActivity.copy(userId = userId))
                        showCreateDialog = false
                    }
                }
            )
        }
    }
}

@Composable
private fun ActivityRowItem(
    activity: UserActivity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    JoyverseCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (activity.isCompleted) Color(0x331E293B) else CosmicCard,
        borderColor = if (activity.isCompleted) NeonEmerald.copy(alpha = 0.3f) else CosmicBorder
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (activity.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle",
                    tint = if (activity.isCompleted) NeonEmerald else StarlightMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (activity.isCompleted) StarlightMuted else StarlightWhite
                    )
                )
                if (activity.description.isNotBlank()) {
                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StarlightMuted,
                            fontSize = 11.sp
                        ),
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ElectricPurple.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = activity.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RadiantCyan,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${activity.estimatedMinutes}m • ${activity.priority} Priority",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StarlightMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = StarlightMuted.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CreateActivityDialog(
    onDismiss: () -> Unit,
    onSave: (UserActivity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Coding") }
    var priority by remember { mutableStateOf("Medium") }
    var duration by remember { mutableStateOf("30") }

    val categories = listOf("Study", "Coding", "Language Learning", "Creativity", "Gaming", "Exercise")
    val priorities = listOf("Low", "Medium", "High")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CosmicCharcoal,
        title = {
            Text(text = "Create Activity", color = StarlightWhite, fontWeight = FontWeight.Bold)
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
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Optional Notes / Details", color = StarlightMuted) },
                    maxLines = 2,
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
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Minutes", color = StarlightMuted) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RadiantCyan,
                            unfocusedBorderColor = CosmicBorder,
                            focusedTextColor = StarlightWhite,
                            unfocusedTextColor = StarlightWhite
                        )
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Priority", style = MaterialTheme.typography.labelSmall.copy(color = RadiantCyan))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            priorities.forEach { p ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (priority == p) CosmicGold else CosmicCard,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { priority = p }
                                ) {
                                    Text(
                                        text = p.take(1),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (priority == p) Color.Black else StarlightWhite,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            UserActivity(
                                title = title.trim(),
                                description = description.trim(),
                                category = category,
                                priority = priority,
                                estimatedMinutes = duration.toIntOrNull() ?: 15
                            )
                        )
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
            ) {
                Text("Save Activity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarlightMuted)
            }
        }
    )
}
