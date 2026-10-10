package com.example.exibirpatrimoniosactivity.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private val LightScheme = lightColorScheme(
    primary = InvScanBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E0FF),
    onPrimaryContainer = Color(0xFF0A0A5C),
    secondary = InvScanGreenDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8F5D9),
    onSecondaryContainer = Color(0xFF00391B),
    background = Color(0xFFF6F6FB),
    onBackground = Color(0xFF1B1B21),
    surface = Color.White,
    onSurface = Color(0xFF1B1B21),
    surfaceVariant = Color(0xFFE4E4F1),
    onSurfaceVariant = Color(0xFF46464F),
    outline = Color(0xFF777680),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF9F9FE),
    surfaceContainer = Color(0xFFF2F2F9),
    surfaceContainerHigh = Color(0xFFECECF4),
    surfaceContainerHighest = Color(0xFFE6E6EF)
)

private val DarkScheme = darkColorScheme(
    primary = InvScanBlueLight,
    onPrimary = Color(0xFF0F0F6B),
    primaryContainer = InvScanBlue,
    onPrimaryContainer = Color(0xFFE0E0FF),
    secondary = Color(0xFF6FE3A1),
    onSecondary = Color(0xFF003919),
    secondaryContainer = Color(0xFF0B5A2E),
    onSecondaryContainer = Color(0xFFC8F5D9),
    background = Color(0xFF111118),
    onBackground = Color(0xFFE4E1E9),
    surface = Color(0xFF1B1B24),
    onSurface = Color(0xFFE4E1E9),
    surfaceVariant = Color(0xFF2C2C38),
    onSurfaceVariant = Color(0xFFC7C5D0),
    outline = Color(0xFF918F9A),
    surfaceContainerLowest = Color(0xFF0E0E15),
    surfaceContainerLow = Color(0xFF17171F),
    surfaceContainer = Color(0xFF1D1D27),
    surfaceContainerHigh = Color(0xFF262631),
    surfaceContainerHighest = Color(0xFF30303C)
)

private val InvScanShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun ExibirPatrimoniosActivityTheme(
    temaEscuro: Boolean = false,
    textoGrande: Boolean = false,
    content: @Composable () -> Unit
) {
    val densidade = LocalDensity.current
    val densidadeAjustada = Density(
        density = densidade.density,
        fontScale = if (textoGrande) densidade.fontScale * 1.3f else densidade.fontScale
    )

    CompositionLocalProvider(LocalDensity provides densidadeAjustada) {
        MaterialTheme(
            colorScheme = if (temaEscuro) DarkScheme else LightScheme,
            typography = Typography,
            shapes = InvScanShapes,
            content = content
        )
    }
}
