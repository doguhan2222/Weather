package com.doguhanay.myweather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.adapters.FutureWeathersAdapter
import com.doguhanay.myweather.adapters.SearchLocationsAdapter
import com.doguhanay.myweather.databinding.FragmentHomeScreenBinding
import com.doguhanay.myweather.viewmodels.HomeScreenViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class HomeScreen : Fragment() {

    lateinit var binding:FragmentHomeScreenBinding

    private val viewModel: HomeScreenViewmodel by viewModels()

    private var futureWeathersAdapter: FutureWeathersAdapter? = null
    private var locationsAdapter: SearchLocationsAdapter? = null

    lateinit var futureRV: RecyclerView
    lateinit var searchRV: RecyclerView

    var cityName:String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeScreenVM = viewModel



        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.

                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.e("denemekonum", location.latitude.toString() + "   " + location.longitude.toString())
                    val gcd = Geocoder(requireContext()!!, Locale.getDefault())
                    val addresses: List<Address>? = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        Log.e("denemekonum2", addresses[0].locality.toString())
                        cityName = addresses[0].locality.toString()
                    }
                    viewModel.fetchWeather(location.latitude.toDouble(), location.longitude.toDouble())
                } else {
                    // Handle the case where the location is null
                    Log.e("denemekonum", "Last known location is null")
                }
            }
        /*fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.e("denemekonum", location?.latitude.toString() + "   " + location?.longitude.toString())
                val gcd = Geocoder(requireContext()!!, Locale.getDefault())
                val addresses: List<Address>? = gcd.getFromLocation(location!!.latitude, location!!.longitude, 1)
                if (addresses!!.size > 0) {
                    Log.e("denemekonum2",addresses!![0].getLocality().toString())
                    cityName = addresses!![0].getLocality().toString()
                }
                viewModel.fetchWeather(location?.latitude?.toDouble()!!,location?.longitude?.toDouble()!!)
            }*/


        futureWeathersAdapter = FutureWeathersAdapter()
        locationsAdapter = SearchLocationsAdapter()

        var date:String = ""
        var time:String = ""
        var code:Int = 0

        viewModel.resultvm.observe(viewLifecycleOwner){ weatherResults ->
            //weatherResults.timezone
            binding.cityNameTV.text = cityName
            binding.temperatureTV.text = weatherResults.currentWeather!!.temperature.toString()
            date = weatherResults.currentWeather!!.time!!.substring(0,10)
            time = weatherResults.currentWeather!!.time!!.substring(11)
            binding.dateTimeTv.text = date + " : " + time
            code = weatherResults.currentWeather!!.weathercode!!
            if ( code == 3){
                binding.animationView.setAnimation(R.raw.sunny)
                binding.animationView.playAnimation()
            }
            if (weatherResults.hourly != null) {
                futureWeathersAdapter?.setList(weatherResults.hourly!!)
            }

        }

        viewModel.locations.observe(viewLifecycleOwner){ locationsResults ->
            if(locationsResults != null){

                locationsAdapter?.setList(locationsResults.results)
                binding.searchRV.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        futureRV = binding.futureWeatherDetailsRV
        futureRV.layoutManager = LinearLayoutManager(requireContext())
        futureRV!!.setHasFixedSize(true)
        futureRV.adapter = futureWeathersAdapter

        searchRV = binding.searchRV
        searchRV.layoutManager = LinearLayoutManager(requireContext())
        searchRV!!.setHasFixedSize(true)
        searchRV.adapter = locationsAdapter

    }


}