package com.android.AnisWeatherApp

data class WeatherResult(
    var name: String,
    var main: MainJson,
    var weather: Array<WeatherJson>,
    val wind: Wind
)

data class Wind(
    val speed: Double,
    val deg: Double,
    val icon: String
)


data class MainJson(
    var temp: Double,
    var pressure: Double,
    var humidity: Double
)

data class WeatherJson(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)