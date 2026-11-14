package com.example.feature.profile

import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.core.data.repository.UserRepository
import com.example.core.designsystem.components.AvatarBadge
import com.example.core.designsystem.components.JoyverseCard
import com.example.core.designsystem.components.XpProgressBar
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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    userId: String,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { UserRepository(context) }
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val profile by repository.observeUserProfile(userId).collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicNavy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EXPLORER PROFILE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StarlightWhite
                    )
                )
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = RadiantCyan.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Pseudonymous",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RadiantCyan,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Avatar & Name Card
            AvatarBadge(
                avatarId = profile?.avatar ?: "avatar_compass",
                size = 88.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "@${profile?.username ?: "Explorer"}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = StarlightWhite
                )
            )

            if (!profile?.bio.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = profile!!.bio,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = StarlightMuted
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gamified Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "LEVEL",
                    value = "${profile?.level ?: 1}",
                    color = ElectricPurple
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "TOTAL XP",
                    value = "${profile?.xp ?: 50}",
                    color = RadiantCyan
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "STREAK",
                    value = "${profile?.streak ?: 1} 🔥",
                    color = CosmicGold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // XP Progression
            JoyverseCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = CosmicCharcoal
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    XpProgressBar(
                        xp = profile?.xp ?: 50,
                        level = profile?.level ?: 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interests & Hobbies
            if (!profile?.interests.isNullOrEmpty()) {
                AlignLeftText(text = "CONNECTED HOBBIES & INTERESTS")
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    profile!!.interests.forEach { interest ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CosmicCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                        ) {
                            Text(
                                text = "✨ $interest",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StarlightWhite,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Privacy Assurance Card
            JoyverseCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0x66161F33),
                borderColor = RadiantCyan.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = RadiantCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Zero-Leakage Pseudonymous Identity",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StarlightWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Email addresses, phone numbers, and Firebase UIDs are never shared in public spaces.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StarlightMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Out Button
            Button(
                onClick = {
                    scope.launch {
                        withContext(NonCancellable) {
                            try {
                                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                            } catch (_: Exception) {}
                            Firebase.auth.signOut()
                            onSignOut()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("sign_out_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CosmicCard,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color
) {
    JoyverseCard(
        modifier = modifier,
        backgroundColor = CosmicCard,
        borderColor = color.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = StarlightMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = color
                )
            )
        }
    }
}

@Composable
private fun AlignLeftText(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = ElectricPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}
