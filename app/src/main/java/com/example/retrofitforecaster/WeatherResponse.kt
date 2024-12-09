package com.example.retrofitforecaster

data class WeatherResponse(
    val list: List<WeatherItem>
)

data class WeatherItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<WeatherDescription>
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String,
    val icon: String
)