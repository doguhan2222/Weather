package com.doguhanay.myweather.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doguhanay.myweather.data.ForecastResponse
import com.doguhanay.myweather.data.Locations
import com.doguhanay.myweather.repository.HomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewmodel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var statusFlag = MutableLiveData<Boolean>(false)

    private val disposablesWeathers = CompositeDisposable()
    private val disposableLocations = CompositeDisposable()

    var searchName = MutableLiveData<String>()
    var currentCityName = MutableLiveData<String>()

    private val _resultvm = MutableLiveData<ForecastResponse>()
    val resultvm: LiveData<ForecastResponse> get() = _resultvm

    private val _locations = MutableLiveData<Locations>()
    val locations: LiveData<Locations> get() = _locations

    private val _searchedResultvm = MutableLiveData<ForecastResponse>()
    val searchedResultWeather: LiveData<ForecastResponse> get() = _searchedResultvm

    private val _favResultvm = MutableLiveData<ForecastResponse>()
    val favResultWeather: LiveData<ForecastResponse> get() = _favResultvm

    fun handleItemClick(latFromRV: Double, longFromRV: Double) {

        homeScreenRepository.getWeather(latFromRV, longFromRV)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> _searchedResultvm.value = result },
                { error ->
                    Log.e(
                        "HomeScreenViewmodelhandleItemClick",
                        "Error fetching weathers: ${error.message}"
                    )
                }
            )
            .addTo(disposablesWeathers)
    }

    fun handleFavItemClick(latFromRV: Double, longFromRV: Double) {
        homeScreenRepository.getWeather(latFromRV, longFromRV)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> _favResultvm.value = result },
                { error ->
                    Log.e(
                        "HomeScreenViewmodelhandleFavItemClick",
                        "Error fetching weathers: ${error.message}"
                    )
                }
            )
            .addTo(disposablesWeathers)
    }


    fun fetchWeather(lat: Double, long: Double) {
        homeScreenRepository.getWeather(lat, long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> _resultvm.value = result },
                { error ->
                    Log.e(
                        "HomeScreenViewmodelfetchWeather",
                        "Error fetching weathers: ${error.message}"
                    )
                }
            )
            .addTo(disposablesWeathers)

    }

    fun addFavorite(lat: Double, long: Double) {
        if (currentCityName != null && currentCityName.value != "") {
            val editor =
                sharedPreferences.edit()
            editor.putString(
                currentCityName.value.toString(),
                lat.toString() + "," + long.toString()
            )
            editor.apply()
        }
    }

    fun deleteFavorite() {
        val editor = sharedPreferences.edit()
        editor.remove(currentCityName.value.toString())
        editor.apply()
    }


    fun addFavVM() {
        statusFlag.value = true
    }


    fun deleteFavVM() {
        statusFlag.value = false


    }

    fun fetchLocations() {
        if(searchName.value != "" && searchName.isInitialized){
            homeScreenRepository.getLocations(searchName.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _locations.value = result },
                    { error ->
                        Log.e(
                            "HomeScreenViewmodelfetchLocations",
                            "Error fetching locations: ${error.message}"
                        )
                    }
                )
                .addTo(disposableLocations)
        }

    }


    override fun onCleared() {
        super.onCleared()
        disposablesWeathers.clear()
        disposableLocations.clear()
    }

}