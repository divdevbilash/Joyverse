package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val JoyverseDarkColorScheme = darkColorScheme(
    primary = ElectricPurple,
    onPrimary = Color.White,
    primaryContainer = ElectricPurpleDark,
    onPrimaryContainer = ElectricPurpleLight,
    secondary = RadiantCyan,
    onSecondary = Color.Black,
    secondaryContainer = RadiantCyanDark,
    onSecondaryContainer = RadiantCyanLight,
    tertiary = AstralBlue,
    onTertiary = Color.White,
    background = CosmicNavy,
    onBackground = StarlightWhite,
    surface = CosmicCharcoal,
    onSurface = StarlightWhite,
    surfaceVariant = CosmicCard,
    onSurfaceVariant = StarlightMuted,
    outline = CosmicBorder,
    outlineVariant = Color(0x1FFFFFFF),
    error = CoralRose,
    onError = Color.White
)

private val JoyverseLightColorScheme = lightColorScheme(
    primary = ElectricPurpleDark,
    onPrimary = Color.White,
    primaryContainer = ElectricPurpleLight,
    onPrimaryContainer = Color(0xFF1E1B4B),
    secondary = RadiantCyanDark,
    onSecondary = Color.White,
    secondaryContainer = RadiantCyanLight,
    onSecondaryContainer = Color(0xFF083344),
    tertiary = AstralBlue,
    onTertiary = Color.White,
    background = JoyverseLightBg,
    onBackground = JoyverseLightText,
    surface = JoyverseLightSurface,
    onSurface = JoyverseLightText,
    surfaceVariant = JoyverseLightCard,
    onSurfaceVariant = JoyverseLightTextMuted,
    outline = Color(0xFFCBD5E1),
    error = CoralRose,
    onError = Color.White
)

@Composable
fun JoyverseTheme(
    darkTheme: Boolean = true, // Default to premium dark cosmic look
    dynamicColor: Boolean = false, // Keep distinctive brand aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> JoyverseDarkColorScheme
        else -> JoyverseLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    JoyverseTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
