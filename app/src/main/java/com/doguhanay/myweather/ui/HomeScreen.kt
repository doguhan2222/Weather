package com.doguhanay.myweather.ui

import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.adapters.FavLocationsAdapter
import com.doguhanay.myweather.adapters.FutureWeathersAdapter
import com.doguhanay.myweather.adapters.SearchLocationsAdapter
import com.doguhanay.myweather.data.RecyclerViewClickListener
import com.doguhanay.myweather.databinding.FragmentHomeScreenBinding
import com.doguhanay.myweather.viewmodels.HomeScreenViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeScreen : Fragment(), RecyclerViewClickListener {

    lateinit var binding: FragmentHomeScreenBinding


    private val viewModel: HomeScreenViewmodel by viewModels()

    private var futureWeathersAdapter: FutureWeathersAdapter? = null
    private var locationsAdapter: SearchLocationsAdapter? = null
    private var favLocationsAdapter: FavLocationsAdapter? = null

    lateinit var futureRV: RecyclerView
    lateinit var searchRV: RecyclerView
    lateinit var favRV: RecyclerView

    var cityName: String = ""
    var lat2 = 0.0
    var long2 = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val ARG_LATITUDE = "latitude"
        private const val ARG_LONGITUDE = "longitude"
        var shared: SharedPreferences? = null
        fun newInstance(latitude: Double, longitude: Double): HomeScreen {
            val fragment = HomeScreen()
            val args = Bundle()
            args.putDouble(ARG_LATITUDE, latitude)
            args.putDouble(ARG_LONGITUDE, longitude)
            fragment.arguments = args
            return fragment
        }
    }

    private val latitude: Double by lazy {
        arguments?.getDouble(ARG_LATITUDE) ?: 0.0
    }
    private val longitude: Double by lazy {
        arguments?.getDouble(ARG_LONGITUDE) ?: 0.0
    }


    override fun onItemClick(latFromAdapter: Double, longFromAdapter: Double, cityName: String) {
        this.cityName = cityName
        this.lat2 = latFromAdapter
        this.long2 = latFromAdapter
        viewModel.handleItemClick(latFromAdapter, longFromAdapter)
    }

    override fun onItemClickFav(latFromAdapter: Double, longFromAdapter: Double, cityName: String) {
        this.cityName = cityName
        this.lat2 = latFromAdapter
        this.long2 = longFromAdapter
        viewModel.handleFavItemClick(latFromAdapter, longFromAdapter)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeScreenVM = viewModel
        /*val editor1 = shared!!.edit()
        editor1.clear()
        editor1.apply()*/


        if (latitude != null && longitude != null) {
            lat2 = latitude
            long2 = longitude
            viewModel.fetchWeather(latitude, longitude)
            val geocoder = Geocoder(requireContext())
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val locationName = address.getAddressLine(0)
                cityName = locationName
            } else {
                cityName = ""
            }

        }


        futureWeathersAdapter = FutureWeathersAdapter()
        locationsAdapter = SearchLocationsAdapter(this)
        favLocationsAdapter = FavLocationsAdapter(shared!!, this)

        val allEntries: Map<String, *>? = shared!!.all
        val count = allEntries?.size ?: 0
        if (count > 0) {
            favLocationsAdapter!!.setFavLocList(allEntries!!.keys.toList())
        }

        var date: String = ""
        var time: String = ""
        var code: Int = 0

        viewModel.resultvm.observe(viewLifecycleOwner) { weatherResults ->
            binding.cityNameTV.text = cityName
            binding.temperatureTV.text = weatherResults.currentWeather!!.temperature.toString()
            date = weatherResults.currentWeather!!.time!!.substring(0, 10)
            time = weatherResults.currentWeather!!.time!!.substring(11)
            binding.dateTimeTv.text = date + " : " + time
            code = weatherResults.currentWeather!!.weathercode!!
            val searchKey = cityName
            val allKeys: Set<String> = shared!!.all.keys
            val count = allKeys?.size ?: 0
            if (allKeys.contains(searchKey)) {
                binding.addFavImageButton.visibility = View.INVISIBLE
                binding.deleteFavImageButton.visibility = View.VISIBLE
                if (count > 0) {
                    favLocationsAdapter!!.setFavLocList(allKeys!!.toList())
                }
            } else {
                binding.addFavImageButton.visibility = View.VISIBLE
                binding.deleteFavImageButton.visibility = View.INVISIBLE
                if (count > 0) {
                    favLocationsAdapter!!.setFavLocList(allKeys!!.toList())
                }
            }
            if (code == 3) {
                binding.animationView.setAnimation(R.raw.sunny)
                binding.animationView.playAnimation()
            }
            if (weatherResults.hourly != null) {
                futureWeathersAdapter?.setList(weatherResults.hourly!!)
            }

        }

        viewModel.searchedResultWeather.observe(viewLifecycleOwner) { searchedWeatherResult ->
            binding.cityNameTV.text = cityName
            binding.temperatureTV.text = searchedWeatherResult.currentWeather!!.temperature.toString()
            date = searchedWeatherResult.currentWeather!!.time!!.substring(0, 10)
            time = searchedWeatherResult.currentWeather!!.time!!.substring(11)
            binding.dateTimeTv.text = date + " : " + time
            code = searchedWeatherResult.currentWeather!!.weathercode!!
            val searchKey = cityName
            val allKeys: Set<String> = shared!!.all.keys
            if (allKeys.contains(searchKey)) {
                binding.addFavImageButton.visibility = View.INVISIBLE
                binding.deleteFavImageButton.visibility = View.VISIBLE
            } else {
                binding.addFavImageButton.visibility = View.VISIBLE
                binding.deleteFavImageButton.visibility = View.INVISIBLE
            }
            if (code == 3) {
                binding.animationView.setAnimation(R.raw.sunny)
                binding.animationView.playAnimation()
            }
            if (searchedWeatherResult.hourly != null) {
                futureWeathersAdapter?.setList(searchedWeatherResult.hourly!!)
            }
            binding.searchBarLayout.visibility = View.VISIBLE
            binding.imageButtonSearch.visibility = View.VISIBLE
            binding.addFavImageButton.visibility = View.VISIBLE
            binding.favPlacesListRv.visibility = View.VISIBLE
            binding.cityNameTV.visibility = View.VISIBLE
            binding.temperatureTV.visibility = View.VISIBLE
            binding.animationView.visibility = View.VISIBLE
            binding.futureWeatherDetailsRV.visibility = View.VISIBLE
            binding.dateTimeTv.visibility = View.VISIBLE

            binding.searchRV.visibility = View.GONE
            binding.editTextSearch.text.clear()
        }

        viewModel.favResultWeather.observe(viewLifecycleOwner) { favLocationWeather ->
            binding.cityNameTV.text = cityName
            binding.temperatureTV.text = favLocationWeather.currentWeather!!.temperature.toString()
            date = favLocationWeather.currentWeather!!.time!!.substring(0, 10)
            time = favLocationWeather.currentWeather!!.time!!.substring(11)
            binding.dateTimeTv.text = date + " : " + time
            code = favLocationWeather.currentWeather!!.weathercode!!
            val searchKey = cityName
            val allKeys: Set<String> = shared!!.all.keys
            if (allKeys.contains(searchKey)) {
                binding.addFavImageButton.visibility = View.INVISIBLE
                binding.deleteFavImageButton.visibility = View.VISIBLE
            } else {
                binding.addFavImageButton.visibility = View.VISIBLE
                binding.deleteFavImageButton.visibility = View.INVISIBLE
            }
            if (code == 3) {
                binding.animationView.setAnimation(R.raw.sunny)
                binding.animationView.playAnimation()
            }
            if (favLocationWeather.hourly != null) {
                futureWeathersAdapter?.setList(favLocationWeather.hourly!!)
            }
            binding.searchBarLayout.visibility = View.VISIBLE
            binding.imageButtonSearch.visibility = View.VISIBLE
            binding.addFavImageButton.visibility = View.VISIBLE
            binding.favPlacesListRv.visibility = View.VISIBLE
            binding.cityNameTV.visibility = View.VISIBLE
            binding.temperatureTV.visibility = View.VISIBLE
            binding.animationView.visibility = View.VISIBLE
            binding.futureWeatherDetailsRV.visibility = View.VISIBLE
            binding.dateTimeTv.visibility = View.VISIBLE

            binding.searchRV.visibility = View.GONE
            binding.editTextSearch.text.clear()
        }


        viewModel.locations.observe(viewLifecycleOwner) { locationsResults ->
            if (locationsResults != null) {
                binding.searchBarLayout.visibility = View.INVISIBLE
                binding.imageButtonSearch.visibility = View.INVISIBLE
                binding.addFavImageButton.visibility = View.INVISIBLE
                binding.favPlacesListRv.visibility = View.INVISIBLE
                binding.cityNameTV.visibility = View.INVISIBLE
                binding.temperatureTV.visibility = View.INVISIBLE
                binding.animationView.visibility = View.INVISIBLE
                binding.futureWeatherDetailsRV.visibility = View.INVISIBLE
                binding.dateTimeTv.visibility = View.INVISIBLE
                binding.addFavImageButton.visibility = View.INVISIBLE
                binding.deleteFavImageButton.visibility = View.INVISIBLE

                locationsAdapter?.setList(locationsResults.results)
                binding.searchRV.visibility = View.VISIBLE
            }
        }

        viewModel.statusFlag.observe(viewLifecycleOwner) { status ->
            if (status != null) {

                if (status == true) {

                    binding.addFavImageButton.visibility = View.INVISIBLE
                    binding.deleteFavImageButton.visibility = View.VISIBLE
                    viewModel.addFavorite(lat2!!, long2!!)
                    val allKeys: Set<String> = shared!!.all.keys

                    if (count >= 0) {
                        favLocationsAdapter!!.setFavLocList(allKeys!!.toList())

                    }
                } else {

                    binding.addFavImageButton.visibility = View.VISIBLE
                    binding.deleteFavImageButton.visibility = View.INVISIBLE
                    viewModel.deleteFavorite()
                    val allKeys: Set<String> = shared!!.all.keys
                    if (count >= 0) {
                        favLocationsAdapter!!.setFavLocList(allKeys!!.toList())

                    }
                }
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

        favRV = binding.favPlacesListRv
        favRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        favRV!!.setHasFixedSize(true)
        favRV.adapter = favLocationsAdapter

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchBarLayout.visibility == View.INVISIBLE) {
                    binding.searchBarLayout.visibility = View.VISIBLE
                    binding.imageButtonSearch.visibility = View.VISIBLE
                    binding.addFavImageButton.visibility = View.VISIBLE
                    binding.favPlacesListRv.visibility = View.VISIBLE
                    binding.cityNameTV.visibility = View.VISIBLE
                    binding.temperatureTV.visibility = View.VISIBLE
                    binding.animationView.visibility = View.VISIBLE
                    binding.futureWeatherDetailsRV.visibility = View.VISIBLE
                    binding.dateTimeTv.visibility = View.VISIBLE

                    binding.searchRV.visibility = View.GONE
                    isEnabled = true // Disable the callback to allow subsequent back button presses
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

}