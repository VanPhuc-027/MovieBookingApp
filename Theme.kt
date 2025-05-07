package com.example.bookingmovie.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.White,
    secondary = Pink80,
    onSecondary = Color.Black,
    background = Color(0xFF121212), // Nền tối hiện đại
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),    // Màu surface tối
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    secondary = Pink40,
    onSecondary = Color.White,
    background = Color(0xFFF5F5F5),  // Nền sáng nhẹ
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),     // Bề mặt trắng tinh
    onSurface = Color.Black,
)

@Composable
fun BookingMovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
