package tk.mallumo.compat.weather.components

sealed class DataState<T>(val entry: T?) {
    class Idle<T>(entry: T? = null) : DataState<T>(entry)
    class Loading<T>(entry: T? = null) : DataState<T>(entry)
    class Result<T>(entry: T) : DataState<T>(entry)
}