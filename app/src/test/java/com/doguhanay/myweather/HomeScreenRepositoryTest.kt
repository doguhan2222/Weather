package com.doguhanay.myweather

import com.doguhanay.myweather.repository.HomeScreenRepository
import org.junit.Test

class HomeScreenRepositoryTest {
    val apiService = MockApiService()
    val homeScreenRepository = HomeScreenRepository(apiService)

    @Test
    fun `test getWeather`() {
        val latitude = 1.23
        val longitude = 4.56

        val testObserver = homeScreenRepository.getWeather(latitude, longitude).test()

        testObserver.assertValue { response ->

            response != null && response.latitude != null && response.latitude!! > 0
        }


        testObserver.assertComplete()
    }


    @Test
    fun `test getLocations`() {
        val name = "London"

        val testObserver = homeScreenRepository.getLocations(name).test()

        testObserver.assertValue { response ->

            response != null && response.results.isNotEmpty()
        }

        testObserver.assertComplete()
    }

}