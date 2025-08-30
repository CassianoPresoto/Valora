package org.example.project.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ValoraColors(
    val positive: Color,        // success/receber
    val positiveSoft: Color,    // success light
    val onPositive: Color,
    val negative: Color,        // danger/dever
    val negativeSoft: Color,    // danger light
    val onNegative: Color,
    val isDark: Boolean
)

val LocalValoraColors = staticCompositionLocalOf {
    ValoraColors(
        positive = Color(0xFF4CAF50),
        positiveSoft = Color(0xFF81C784),
        onPositive = Color(0xFFFFFFFF),
        negative = Color(0xFFF44336),
        negativeSoft = Color(0xFFE57373),
        onNegative = Color(0xFFFFFFFF),
        isDark = false
    )
}

fun valoraLightColors() = ValoraColors(
    positive = Color(0xFF2E7D32),     // Green 800
    positiveSoft = Color(0xFF81C784), // Green 300
    onPositive = Color(0xFFFFFFFF),
    negative = Color(0xFFC62828),     // Red 800
    negativeSoft = Color(0xFFE57373), // Red 300
    onNegative = Color(0xFFFFFFFF),
    isDark = false
)

fun valoraDarkColors() = ValoraColors(
    positive = Color(0xFF81C784),     // Lighter green for dark bg
    positiveSoft = Color(0xFFA5D6A7),
    onPositive = Color(0xFF003314),
    negative = Color(0xFFE57373),
    negativeSoft = Color(0xFFEF9A9A),
    onNegative = Color(0xFF3F0000),
    isDark = true
)

object ValoraTheme {
    val colors: ValoraColors
        @Composable get() = LocalValoraColors.current
}
