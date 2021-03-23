package tk.mallumo.compat.weather.components

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import tk.mallumo.compat.weather.components.repository.response.RespWeather
import java.time.format.DateTimeFormatter


class Weather(
    var dt_raw: String = "",
    var date: String = "",
    var time: String = "",
    var symbol: String = "",
    var wind_direction: Double = 0.0,

    var temperature: Double = 0.0,
    var temperature_unit: String = "",

    var wind: Double = 0.0,
    var wind_unit: String = "",

    var fog: Double = 0.0,
    var fog_unit: String = "",

    var cloud: Double = 0.0,
    var cloud_unit: String = "",

    var humidity: Double = 0.0,
    var humidity_unit: String = "",
)

val Weather.descriptionFull: String
    get() = """
    $date $time
    Temperature:    $temperature $temperature_unit
    Humidity:       $humidity $humidity_unit
    Winding:        $wind $wind_unit
    Wind direction: $wind_direction
    Fog:            $fog $fog_unit
    Clouds:         $cloud $cloud_unit
    """.trimIndent()

fun RespWeather.PropertiesTimeItem.weather(units: RespWeather.MetaUnits): Weather {
    val dt = Instant.parse(time)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()

    return Weather(
        dt_raw = time,
        date = dt.format(DateTimeFormatter.ISO_LOCAL_DATE),
        time = "${dt.hour}:${dt.minute.toString().padStart(2, '0')}",
        symbol = data.next_1_hours.summary.symbol_code,
        wind_direction = data.instant.details.wind_from_direction,

        temperature = data.instant.details.air_temperature,
        temperature_unit = units.air_temperature.let {
            if (it == "celsius") "°"
            else it
        },

        wind = data.instant.details.wind_speed,
        wind_unit = units.wind_speed,

        fog = data.instant.details.fog_area_fraction,
        fog_unit = units.fog_area_fraction,

        cloud = data.instant.details.cloud_area_fraction,
        cloud_unit = units.cloud_area_fraction,

        humidity = data.instant.details.relative_humidity,
        humidity_unit = units.relative_humidity,
    )
}
