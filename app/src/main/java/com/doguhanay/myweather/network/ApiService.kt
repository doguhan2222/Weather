package com.doguhanay.myweather.network


import com.doguhanay.myweather.data.ForecastResponse
import com.doguhanay.myweather.data.Locations
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("forecast")
    fun getForecast(
        @Query("latitude") latitude:Double,
        @Query("longitude") longitude:Double,
        @Query("hourly") hourly:String = "temperature_2m,weathercode",
        @Query("models") models:String = "best_match",
        @Query("current_weather") currentWeather:Boolean = true,
        @Query("forecast_days") forecastDays:Int = 1,
        @Query("timezone") timezone:String = "auto"
    ): Single<ForecastResponse>

    @GET
    fun searchLocation(@Url url: String): Single<Locations>
}