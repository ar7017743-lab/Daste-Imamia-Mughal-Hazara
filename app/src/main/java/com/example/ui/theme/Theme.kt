package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DasteDarkColorScheme = darkColorScheme(
    primary = BurnishedGold,
    onPrimary = ObsidianBlack,
    primaryContainer = MutedGoldTint,
    onPrimaryContainer = ChampagneGold,
    secondary = ChampagneGold,
    onSecondary = ObsidianBlack,
    secondaryContainer = ElevatedObsidian,
    onSecondaryContainer = IvoryWhite,
    tertiary = AntiqueGold,
    onTertiary = IvoryWhite,
    background = ObsidianBlack,
    onBackground = IvoryWhite,
    surface = CharcoalSurface,
    onSurface = IvoryWhite,
    surfaceVariant = ElevatedObsidian,
    onSurfaceVariant = LightMutedText,
    outline = SoftObsidianBorder,
    outlineVariant = Color(0xFF242017),
    error = Color(0xFFE57373),
    onError = ObsidianBlack
)

private val DasteLightColorScheme = lightColorScheme(
    primary = AntiqueGold,
    onPrimary = Color.White,
    primaryContainer = WarmParchment,
    onPrimaryContainer = InkBlack,
    secondary = BurnishedGold,
    onSecondary = ObsidianBlack,
    secondaryContainer = WarmParchment,
    onSecondaryContainer = InkBlack,
    tertiary = ObsidianBlack,
    onTertiary = ChampagneGold,
    background = IvoryWhite,
    onBackground = InkBlack,
    surface = Color.White,
    onSurface = InkBlack,
    surfaceVariant = WarmParchment,
    onSurfaceVariant = CharcoalMuted,
    outline = SoftCreamBorder,
    outlineVariant = Color(0xFFECE4D0),
    error = DeepCrimsonAccent,
    onError = Color.White
)

val DasteShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(26.dp)
)

@Composable
fun DasteImamiaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DasteDarkColorScheme else DasteLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = DasteShapes,
        content = content
    )
}
