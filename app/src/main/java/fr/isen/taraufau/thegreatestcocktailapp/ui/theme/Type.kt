package fr.isen.taraufau.thegreatestcocktailapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.isen.taraufau.thegreatestcocktailapp.R

val LobsterFont = FontFamily(Font(R.font.lobster))
val PacificoFont = FontFamily(Font(R.font.pacifico))
val RighteousFont = FontFamily(Font(R.font.righteous))

val AppTypography = Typography(
    // titleLarge : On l'utilisera pour les titres de la TopAppBar
    titleLarge = TextStyle(
        fontFamily = LobsterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    ),

    // headlineMedium : On l'utilisera pour le nom du cocktail dans le détail
    headlineMedium = TextStyle(
        fontFamily = PacificoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),

    // Pour tout le reste
    bodyLarge = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)