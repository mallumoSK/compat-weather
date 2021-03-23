package tk.mallumo.compat.weather.components.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
@DrawableRes
fun ImageBitmap.Companion.imageResId(key: String): Int {
    val context = LocalContext.current
    return remember(key) {
        try {
            context.resources.getIdentifier(key, "drawable", context.packageName)
        } catch (e: Exception) {
            -1
        }
    }
}
