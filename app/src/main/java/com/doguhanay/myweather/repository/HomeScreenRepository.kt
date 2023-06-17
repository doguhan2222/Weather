package com.doguhanay.myweather.repository

import com.doguhanay.myweather.data.ForecastResponse
import com.doguhanay.myweather.data.Locations
import com.doguhanay.myweather.network.ApiService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HomeScreenRepository @Inject constructor(private val apiService: ApiService) {

    fun getWeather(latitude:Double,longitude:Double): Single<ForecastResponse> {
        return apiService.getForecast(latitude,longitude)
    }
    fun getLocations(name:String):Single<Locations>{
        val baseUrl = "https://geocoding-api.open-meteo.com/v1/search"

        val completeUrl = "$baseUrl?name=$name&count=20&language=en&format=json"
        return apiService.searchLocation(completeUrl)
    }
}