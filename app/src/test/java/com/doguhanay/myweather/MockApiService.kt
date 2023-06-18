package com.doguhanay.myweather

import com.doguhanay.myweather.data.CurrentWeather
import com.doguhanay.myweather.data.ForecastResponse
import com.doguhanay.myweather.data.Hourly
import com.doguhanay.myweather.data.HourlyUnits
import com.doguhanay.myweather.data.Locations
import com.doguhanay.myweather.data.LocationsResult
import com.doguhanay.myweather.network.ApiService
import io.reactivex.rxjava3.core.Single

class MockApiService : ApiService {
    override fun getForecast(
        latitude: Double,
        longitude: Double,
        hourly: String,
        models: String,
        currentWeather: Boolean,
        forecastDays: Int,
        timezone: String
    ): Single<ForecastResponse> {
        val mockCurrentWeather = CurrentWeather()

        val mockHourlyUnits = HourlyUnits()

        val mockHourly = Hourly(arrayListOf(), arrayListOf(), arrayListOf())

        val mockResponse = ForecastResponse(
            latitude = 1.23,
            longitude = longitude,
            generationtimeMs = 123.45,
            utcOffsetSeconds = 0,
            timezone = "GMT",
            timezoneAbbreviation = "GMT",
            elevation = null,
            currentWeather = mockCurrentWeather,
            hourlyUnits = mockHourlyUnits,
            hourly = mockHourly
        )

        return Single.just(mockResponse)
    }

    override fun searchLocation(url: String): Single<Locations> {
        val mockLocationResult = LocationsResult()

        val mockLocations = Locations(arrayListOf(mockLocationResult))

        return Single.just(mockLocations)
    }
}

