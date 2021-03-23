package tk.mallumo.compat.weather.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Teal500,
    primaryVariant = Teal700,
    secondary = Cyan500,

    surface = BlueGray800,
    background = Gray900,

    onPrimary = Color.White,
    onSecondary = Color.Black,

    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun CompatWeatherTheme(content: @Composable() () -> Unit) {

    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}