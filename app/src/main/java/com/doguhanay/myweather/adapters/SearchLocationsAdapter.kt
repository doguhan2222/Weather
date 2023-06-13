package com.doguhanay.myweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.data.Hourly
import com.doguhanay.myweather.data.Locations
import com.doguhanay.myweather.data.LocationsResult
import com.doguhanay.myweather.databinding.SearchResultRvBinding

class SearchLocationsAdapter () :RecyclerView.Adapter<SearchLocationsAdapter.LocationsViewHolder> (){
    private var locationsList: List<LocationsResult>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLocationsAdapter.LocationsViewHolder {
        val locationsListBinding = DataBindingUtil.inflate<SearchResultRvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_result_rv,parent,false
        )
        return LocationsViewHolder(locationsListBinding)
    }

    override fun onBindViewHolder(holder: SearchLocationsAdapter.LocationsViewHolder, position: Int) {

        var currentListItem = locationsList!![position]

        var currentCityName:String = currentListItem.name.toString()
        var currentAreaName:String = currentListItem.admin1.toString()
        var currentCountryName:String = currentListItem.country.toString()

        holder.bindLocations(currentCityName,currentAreaName,currentCountryName)
    }

    override fun getItemCount(): Int {
        return if (locationsList != null) {
            locationsList!!.size
        } else {
            0
        }


    }
    fun setList(locationsListFragment: List<LocationsResult>) {
        this.locationsList = locationsListFragment
        notifyDataSetChanged()
    }

    inner class LocationsViewHolder(private val locationsListBinding: SearchResultRvBinding):
            RecyclerView.ViewHolder(locationsListBinding.root){
                fun bindLocations(currentCityName:String, currentAreaName:String,  currentCountryName:String){
                    locationsListBinding.searchCityName.text = currentCityName
                    locationsListBinding.searchAreaName.text = currentAreaName
                    locationsListBinding.searchCountryName.text = currentCountryName
                }
            }
}