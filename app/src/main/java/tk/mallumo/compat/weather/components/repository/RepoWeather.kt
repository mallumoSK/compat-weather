package tk.mallumo.compat.weather.components.repository

import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tk.mallumo.compat.weather.R
import tk.mallumo.compat.weather.app
import tk.mallumo.compat.weather.components.DataState
import tk.mallumo.compat.weather.components.repository.response.RespWeather
import tk.mallumo.compat.weather.components.weather

class RepoWeather {

    suspend fun get() = flow {
        emit(DataState.Loading())
        val rawWeather = app.resources.openRawResource(R.raw.weather)
            .bufferedReader()
            .use { reader ->
                Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<RespWeather>(reader.readText())
            }

        val output = rawWeather.properties.timeseries
            .map {
                it.weather(rawWeather.properties.meta.units)
            }

        emit(DataState.Result(output))
    }
}