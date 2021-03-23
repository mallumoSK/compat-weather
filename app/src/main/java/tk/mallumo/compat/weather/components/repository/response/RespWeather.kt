package tk.mallumo.compat.weather.components.repository.response

import kotlinx.serialization.Serializable

@Serializable
@Suppress("unused")
class RespWeather(var properties: Properties = Properties()) {

    @Serializable
    class Properties(
        var meta: PropertiesMeta = PropertiesMeta(),
        var timeseries: Array<PropertiesTimeItem> = arrayOf()
    )

    @Serializable
    class PropertiesMeta(
        val updated_at: String = "",
        val units: MetaUnits = MetaUnits()
    )

    @Serializable
    class MetaUnits(
        val air_pressure_at_sea_level: String = "hPa",
        val air_temperature: String = "celsius",
        val air_temperature_max: String = "celsius",
        val air_temperature_min: String = "celsius",
        val cloud_area_fraction: String = "%",
        val cloud_area_fraction_high: String = "%",
        val cloud_area_fraction_low: String = "%",
        val cloud_area_fraction_medium: String = "%",
        val dew_point_temperature: String = "celsius",
        val fog_area_fraction: String = "%",
        val precipitation_amount: String = "mm",
        val relative_humidity: String = "%",
        val ultraviolet_index_clear_sky: String = "1",
        val wind_from_direction: String = "degrees",
        val wind_speed: String = "m/s"
    )

    @Serializable
    class PropertiesTimeItem(
        var time: String = "",
        var data: TimeItemData = TimeItemData()
    )

    @Serializable
    class TimeItemData(
        var instant: DataInstant = DataInstant(),
        var next_1_hours: DataSummaryHolder = DataSummaryHolder()
    )


    @Serializable
    class DataInstant(var details: InstantDetails = InstantDetails())

    @Serializable
    class InstantDetails(
//        var air_pressure_at_sea_level:Double = 0.0,
        var air_temperature: Double = 0.0,
        var cloud_area_fraction: Double = 0.0,
//        var cloud_area_fraction_high:Double = 0.0,
//        var cloud_area_fraction_low:Double = 0.0,
//        var cloud_area_fraction_medium:Double = 0.0,
        var dew_point_temperature: Double = 0.0,
        var fog_area_fraction: Double = 0.0,
        var relative_humidity: Double = 0.0,
        var ultraviolet_index_clear_sky: Double = 0.0,
        var wind_from_direction: Double = 0.0,
        var wind_speed: Double = 0.0,
    )

    @Serializable
    class DataSummaryHolder(var summary: HolderSummary = HolderSummary())

    @Serializable
    class HolderSummary(var symbol_code: String = "")


}