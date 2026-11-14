package com.example.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCharcoal
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.RadiantCyan
import com.example.ui.theme.StarlightMuted
import com.example.ui.theme.StarlightWhite

val AvatarPresets = listOf(
    AvatarPreset("avatar_compass", "🧭", "Cosmic Compass", Color(0xFF8B5CF6)),
    AvatarPreset("avatar_telescope", "🔭", "Astral Seeker", Color(0xFF06B6D4)),
    AvatarPreset("avatar_robot", "🤖", "Cyber Tinkerer", Color(0xFF3B82F6)),
    AvatarPreset("avatar_artist", "🎨", "Creative Muse", Color(0xFFEC4899)),
    AvatarPreset("avatar_globe", "🌍", "World Voyager", Color(0xFF10B981)),
    AvatarPreset("avatar_controller", "🎮", "Universe Gamer", Color(0xFFF59E0B)),
    AvatarPreset("avatar_sparkles", "✨", "Curiosity Spark", Color(0xFFA855F7)),
    AvatarPreset("avatar_book", "📚", "Knowledge Keeper", Color(0xFF6366F1))
)

data class AvatarPreset(
    val id: String,
    val emoji: String,
    val name: String,
    val color: Color
)

@Composable
fun JoyverseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    testTag: String = "joyverse_button"
) {
    val gradient = Brush.horizontalGradient(
        colors = if (enabled) listOf(ElectricPurple, RadiantCyan) else listOf(Color(0xFF334155), Color(0xFF475569))
    )

    Surface(
        modifier = modifier
            .testTag(testTag)
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun JoyverseCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CosmicCard,
    borderColor: Color = CosmicBorder,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
        color = backgroundColor,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        content()
    }
}

@Composable
fun AvatarBadge(
    avatarId: String,
    size: Dp = 56.dp,
    showBorder: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val preset = AvatarPresets.find { it.id == avatarId } ?: AvatarPresets.first()
    val modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .background(
            Brush.radialGradient(
                colors = listOf(preset.color.copy(alpha = 0.4f), CosmicCharcoal)
            )
        )
        .then(
            if (showBorder) Modifier.border(2.dp, preset.color, CircleShape) else Modifier
        )
        .then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = preset.emoji,
            fontSize = (size.value * 0.48).sp
        )
    }
}

@Composable
fun XpProgressBar(
    xp: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    val xpInLevel = xp % 100
    val progress = (xpInLevel / 100f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "xp_progress"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ElectricPurple.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricPurple)
                ) {
                    Text(
                        text = "LVL $level",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ElectricPurple,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Explorer Universe",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = StarlightMuted,
                        fontSize = 12.sp
                    )
                )
            }
            Text(
                text = "$xpInLevel / 100 XP",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = RadiantCyan,
                    fontSize = 12.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF1E293B))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(ElectricPurple, RadiantCyan)
                        )
                    )
            )
        }
    }
}
