package fr.isen.taraufau.thegreatestcocktailapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CocktailDarkColorScheme = darkColorScheme(
    primary = NeonPinkPrimary,
    secondary = NeonCyanSecondary,
    background = DeepPurpleBackground,
    surface = SurfacePurpleCard,
    surfaceVariant = SurfacePurpleCard, // Pour la barre de recherche et certains fonds
    onPrimary = TextWhite,
    onSecondary = DeepPurpleBackground,
    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextGray
)

@Composable
fun TheGreatestCocktailAppTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepPurpleBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = CocktailDarkColorScheme,
        content = content,
        typography = AppTypography
    )
}