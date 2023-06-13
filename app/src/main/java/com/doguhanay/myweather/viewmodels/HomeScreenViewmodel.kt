package com.doguhanay.myweather.viewmodels

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
//private val context: Context
@HiltViewModel
class HomeScreenViewmodel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository, ) : ViewModel() {

    private val disposablesWeathers = CompositeDisposable()
    private val disposableLocations = CompositeDisposable()

    var searchName = MutableLiveData<String>()

    private val _resultvm = MutableLiveData<ForecastResponse>()
    val resultvm: LiveData<ForecastResponse> get() = _resultvm

    private val _locations = MutableLiveData<Locations>()
    val locations: LiveData<Locations> get() = _locations



    fun fetchWeather(lat:Double,long:Double) {//38.40 27.17

        // koordinatları buradan yollicaz
        homeScreenRepository.getWeather(lat,long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> _resultvm.value = result },
                { error -> Log.e("HomeScreenViewmodel", "Error fetching weathers: ${error.message}") }
            )
            .addTo(disposablesWeathers)

    }
    // api response de bir sıkıntı var.
    fun fetchLocations(){
        homeScreenRepository.getLocations(searchName.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> _locations.value = result},
                { error -> Log.e("HomeScreenViewmodel", "Error fetching locations: ${error.message}") }
            )
            .addTo(disposableLocations)
    }


    override fun onCleared() {
        super.onCleared()
        disposablesWeathers.clear()
        disposableLocations.clear()
    }

}