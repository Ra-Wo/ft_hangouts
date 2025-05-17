package com.rachid.ft_hangouts.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// constants
const val THEME_KEY = "theme_key"
const val THEME_PREF_KEY = "theme_pref"

// Purple theme
val PurpleColorScheme = lightColorScheme(
    primary = Color(0xFF6650a4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFF625b71),
    onSecondary = Color.White,
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
)

val PurpleDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF1C1B1F),
    primaryContainer = Color(0xFF4F378B),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF1C1B1F),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF1C1B1F),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
)


// Red Theme
val RedColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    secondary = Color(0xFFC2185B),
    onSecondary = Color.White,
    tertiary = Color(0xFFFF5252),
    onTertiary = Color.Black,
    background = Color(0xFFFFEBEE),
    onBackground = Color.Black,
    surface = Color(0xFFFFEBEE),
    onSurface = Color.Black
)

val RedDarkColorScheme = darkColorScheme(
    primary = Color(0xFFEF5350),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFB71C1C),
    secondary = Color(0xFFE91E63),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFF8A80),
    onTertiary = Color.Black,
    background = Color(0xFF212121),
    onBackground = Color.White,
    surface = Color(0xFF303030),
    onSurface = Color.White
)


// Green Theme
val GreenColorScheme = lightColorScheme(
    primary = Color(0xFF388E3C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF2E7D32),
    onSecondary = Color.White,
    tertiary = Color(0xFF66BB6A),
    onTertiary = Color.Black,
    background = Color(0xFFE8F5E9),
    onBackground = Color.Black,
    surface = Color(0xFFE8F5E9),
    onSurface = Color.Black
)

val GreenDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF66BB6A),
    onSecondary = Color.Black,
    tertiary = Color(0xFFA5D6A7),
    onTertiary = Color.Black,
    background = Color(0xFF212121),
    onBackground = Color.White,
    surface = Color(0xFF2E7D32),
    onSurface = Color.White
)

// Blue Theme
val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFF0288D1),
    onSecondary = Color.White,
    tertiary = Color(0xFF81D4FA),
    onTertiary = Color.Black,
    background = Color(0xFFE3F2FD),
    onBackground = Color.Black,
    surface = Color(0xFFE3F2FD),
    onSurface = Color.Black
)

val BlueDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFF4FC3F7),
    onSecondary = Color.Black,
    tertiary = Color(0xFF29B6F6),
    onTertiary = Color.Black,
    background = Color(0xFF102027),
    onBackground = Color.White,
    surface = Color(0xFF1E2A38),
    onSurface = Color.White
)




enum class ThemeType(val lightColorScheme: ColorScheme, val darkColorScheme: ColorScheme) {
    PURPLE(PurpleColorScheme, PurpleDarkColorScheme),
    RED(RedColorScheme, RedDarkColorScheme),
    GREEN(GreenColorScheme, GreenDarkColorScheme),
    BLUE(BlueColorScheme, BlueDarkColorScheme),
}


@Composable
fun Ft_hangoutsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    theme: ThemeType = ThemeType.PURPLE,
    content: @Composable () -> Unit
) {

//    val themeManager = ThemeManager(LocalContext.current)
//    val themeType = themeManager.getTheme()
    val colorScheme = when {
        // if dynamic color is enabled and the device is running Android 12 or higher
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> theme.darkColorScheme
        else -> theme.lightColorScheme
    }

    // Apply the theme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


class ThemeManager(context: Context) {
    private val sharedPref = context.getSharedPreferences(THEME_PREF_KEY, Context.MODE_PRIVATE)

    fun setTheme(themeType: ThemeType) {
        sharedPref.edit().apply {
            putString(THEME_KEY, themeType.name)
            apply()
        }
    }

    fun getTheme(): ThemeType {
        val themeTypeName = sharedPref.getString(THEME_KEY, ThemeType.PURPLE.name)
        return ThemeType.valueOf(themeTypeName ?: ThemeType.PURPLE.name)
    }
}
