package org.example.project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.example.project.resources.Res
import org.example.project.resources.pacifico_regular
import org.example.project.resources.philosopher_bold
import org.example.project.resources.philosopher_regular
import org.jetbrains.compose.resources.Font

/** Pacifico - для заголовків, Philosopher - для основного тексту. Обидва мають кирилицю (OFL). */
@Composable
fun fridgeQuestTypography(): Typography {
    val heading = FontFamily(Font(Res.font.pacifico_regular))
    val body = FontFamily(
        Font(Res.font.philosopher_regular, FontWeight.Normal),
        Font(Res.font.philosopher_bold, FontWeight.Bold),
    )
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = heading),
        displayMedium = base.displayMedium.copy(fontFamily = heading),
        displaySmall = base.displaySmall.copy(fontFamily = heading),
        headlineLarge = base.headlineLarge.copy(fontFamily = heading),
        headlineMedium = base.headlineMedium.copy(fontFamily = heading),
        headlineSmall = base.headlineSmall.copy(fontFamily = heading),
        titleLarge = base.titleLarge.copy(fontFamily = body, fontWeight = FontWeight.Bold),
        titleMedium = base.titleMedium.copy(fontFamily = body, fontWeight = FontWeight.Bold),
        titleSmall = base.titleSmall.copy(fontFamily = body, fontWeight = FontWeight.Bold),
        bodyLarge = base.bodyLarge.copy(fontFamily = body),
        bodyMedium = base.bodyMedium.copy(fontFamily = body),
        bodySmall = base.bodySmall.copy(fontFamily = body),
        labelLarge = base.labelLarge.copy(fontFamily = body, fontWeight = FontWeight.Bold),
        labelMedium = base.labelMedium.copy(fontFamily = body),
        labelSmall = base.labelSmall.copy(fontFamily = body),
    )
}
