package tk.mallumo.compat.weather.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tk.mallumo.compat.weather.R
import tk.mallumo.compat.weather.components.DataState
import tk.mallumo.compat.weather.components.Weather
import tk.mallumo.compat.weather.components.descriptionFull
import tk.mallumo.compat.weather.components.repository.Repo
import tk.mallumo.compat.weather.components.ui.Space
import tk.mallumo.compat.weather.components.ui.imageResId
import tk.mallumo.compose.navigation.ComposableNavNode
import tk.mallumo.compose.navigation.NavigationViewModel
import tk.mallumo.compose.navigation.navigationViewModel
import tk.mallumo.puppy.ui.LoaderOverlay

class HomeVM : NavigationViewModel() {

    val weather: StateFlow<DataState<Map<String, List<Weather>>>> get() = weatherInternal
    private val weatherInternal: MutableStateFlow<DataState<Map<String, List<Weather>>>> = MutableStateFlow(DataState.Idle())


    override fun onCleared() {
        weatherInternal.value = DataState.Idle()
    }

    fun reload() {
        viewModelScope.launch {
            Repo.weather.get().collect {
                when (it) {
                    is DataState.Loading -> {
                        weatherInternal.value = DataState.Loading(weatherInternal.value.entry)
                    }
                    is DataState.Result -> {
                        weatherInternal.value = DataState.Result(
                            it.entry!!.groupBy { it.date }
                                .filter { it.value.isNotEmpty() })
                    }
                }
            }
        }
    }
}

@Composable
@ComposableNavNode
fun HomeUI() {
    val vm = navigationViewModel<HomeVM>()
    ContentHomeUI(vm)
    SideEffect {
        vm.reload()
    }
}

@Composable
private fun ContentHomeUI(vm: HomeVM) {
    val weather = vm.weather.collectAsState()
    when (weather.value) {
        is DataState.Result -> InternalHomeUI(weather)
        else -> LoaderOverlay()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InternalHomeUI(
    weather: State<DataState<Map<String, List<Weather>>>>
) {
    val currentWeather = remember {
        mutableStateOf(weather.value.entry!!.values.first().first())
    }
    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        WeatherInfo(currentWeather)
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            weather.value.entry?.values?.forEachIndexed { indexGroup, entry ->
                stickyHeader {
                    InfoPageHeader(entry.first())
                }
                itemsIndexed(entry) { indexItem, item ->
                    WeatherItem(item) {
                        currentWeather.value = item
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherInfo(currentWeather: MutableState<Weather>) {
    Column(
        Modifier
            .semantics(true) {
                contentDescription = """
                    San francisko
                    ${currentWeather.value.descriptionFull}
                """.trimIndent()
            }
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 16.dp)
    ) {
        WeatherInfoMain(currentWeather.value)
        Space(size = 16.dp)
        WeatherInfoBody(currentWeather.value)
    }
}

@Composable
private fun WeatherInfoMain(value: Weather) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "San francisko",
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = "${value.temperature}${value.temperature_unit}",
            style = MaterialTheme.typography.h4
        )
        Space(size = 16.dp)
        if (value.symbol.isNotEmpty()) {
            Image(
                bitmap = ImageBitmap.imageResource(ImageBitmap.imageResId(key = value.symbol)),
                contentDescription = value.symbol,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}


@Composable
private fun WeatherInfoBody(item: Weather, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        WeatherInfoBodyElement(
            Modifier.weight(1F),
            R.drawable.ic_wind,
            "${item.wind}${item.wind_unit}"
        )
        WeatherInfoBodyElement(
            Modifier.weight(1F),
            R.drawable.ic_humidity,
            "${item.humidity}${item.humidity_unit}"
        )
        WeatherInfoBodyElement(
            Modifier.weight(1F),
            R.drawable.ic_fog,
            "${item.fog}${item.fog_unit}"
        )
        WeatherInfoBodyElement(
            Modifier.weight(1F),
            R.drawable.ic_clouds,
            "${item.cloud}${item.cloud_unit}"
        )
    }
}

@Composable
private fun WeatherInfoBodyElement(
    modifier: Modifier,
    @DrawableRes iconRes: Int, value: String
) {
    Column(modifier) {
        Image(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun InfoPageHeader(weather: Weather) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CutCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp))
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = weather.date,
            style = MaterialTheme.typography.caption,
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun WeatherItem(item: Weather, onItemActivation: () -> Unit) {
    val density = LocalDensity.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clip(CutCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = item.descriptionFull
            }
            .onGloballyPositioned {
                val possition = (it.positionInParent().y / density.density).toInt()
                if (possition in 0..80) {
                    onItemActivation()
                }
            }) {
        WeatherItemBody(item)
    }

}


@Composable
private fun WeatherItemBody(item: Weather, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = item.time,
            style = MaterialTheme.typography.h6,
        )
        Spacer(modifier = Modifier.weight(1F))

        Text(
            text = "${item.temperature}${item.temperature_unit}",
            style = MaterialTheme.typography.h6
        )
        Space(size = 16.dp)
        if (item.symbol.isNotEmpty()) {
            Image(
                bitmap = ImageBitmap.imageResource(ImageBitmap.imageResId(key = item.symbol)),
                contentDescription = item.symbol.replace("_", " "),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
