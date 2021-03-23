package tk.mallumo.compat.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tk.mallumo.compat.weather.ui.theme.CompatWeatherTheme
import tk.mallumo.compose.navigation.HomeUI
import tk.mallumo.compose.navigation.NavigationContent
import tk.mallumo.compose.navigation.Node

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompatWeatherTheme {
                NavigationContent(startupNode = Node.HomeUI)
            }
        }
    }
}