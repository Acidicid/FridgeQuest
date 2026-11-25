package org.example.project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Кольори з дизайн-системи Stitch: глибокий бірюзовий + темний бурштин
private val Teal = Color(0xFF00685F)
private val TealContainer = Color(0xFF00857A)
private val TealLight = Color(0xFF6BD8CB)
private val Amber = Color(0xFF904D00)
private val AmberContainer = Color(0xFFFD9A2E)
private val Terracotta = Color(0xFF9F4628)
private val TerracottaContainer = Color(0xFFBE5E3D)
private val Ink = Color(0xFF181C1C)

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = Color.White,
    inversePrimary = TealLight,
    secondary = Amber,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = Color(0xFF3E2000),
    tertiary = Terracotta,
    onTertiary = Color.White,
    tertiaryContainer = TerracottaContainer,
    onTertiaryContainer = Color.White,
    background = Color(0xFFF8FAF9),
    onBackground = Ink,
    surface = Color(0xFFF8FAF9),
    onSurface = Ink,
    surfaceVariant = Color(0xFFDDE4E2),
    onSurfaceVariant = Color(0xFF3E4947),
    outline = Color(0xFF6E7977),
    error = Color(0xFFBA1A1A),
)

private val DarkColors = darkColorScheme(
    primary = TealLight,
    onPrimary = Color(0xFF003732),
    primaryContainer = Color(0xFF00504A),
    onPrimaryContainer = Color(0xFF8BF5E7),
    secondary = Color(0xFFFFB77C),
    onSecondary = Color(0xFF4D2600),
    secondaryContainer = Amber,
    onSecondaryContainer = Color(0xFFFFDCC2),
    tertiary = Color(0xFFFFB59D),
    onTertiary = Color(0xFF5C1A03),
    tertiaryContainer = Terracotta,
    onTertiaryContainer = Color(0xFFFFDBD0),
    background = Color(0xFF101414),
    onBackground = Color(0xFFE0E3E2),
    surface = Color(0xFF101414),
    onSurface = Color(0xFFE0E3E2),
    surfaceVariant = Color(0xFF3E4947),
    onSurfaceVariant = Color(0xFFBDC9C6),
    outline = Color(0xFF889391),
    error = Color(0xFFFFB4AB),
)

// Заокруглені картки й кнопки
private val FridgeShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
)

@Composable
fun FridgeQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = fridgeQuestTypography(),
        shapes = FridgeShapes,
        content = content,
    )
}
