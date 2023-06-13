package com.doguhanay.myweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.data.Hourly
import com.doguhanay.myweather.databinding.FutureWeathersRvBinding
import com.doguhanay.myweather.viewmodels.HomeScreenViewmodel
import javax.inject.Inject

class FutureWeathersAdapter () : RecyclerView.Adapter<FutureWeathersAdapter.WeatherViewHolder>() {
    private var weatherList: Hourly? = null
    var date:String = ""
    var time:String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {

        val futureWeatherListBinding = DataBindingUtil.inflate<FutureWeathersRvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.future_weathers_rv,parent,false
        )
        return WeatherViewHolder(futureWeatherListBinding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        var currentTimeDate:String = weatherList!!.time[position]
        var currentTempature:String = weatherList!!.temperature2m[position].toString()
        var currentWeatherCode:String = weatherList!!.weathercode[position].toString()
        holder.bind(currentTimeDate,currentTempature,currentWeatherCode)
    }

    override fun getItemCount(): Int {

        return weatherList?.weathercode?.size ?: 0
    }

    fun setList(weatherListFragment: Hourly) {
        this.weatherList = weatherListFragment
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(private val futureWeatherListBinding: FutureWeathersRvBinding) :
        RecyclerView.ViewHolder(futureWeatherListBinding.root) {

        fun bind( timeDate:String, tempature:String,  code:String) {
            date = timeDate.substring(0,10)
            time = timeDate.substring(11)
            futureWeatherListBinding.futureRVDate.text = date + " : "+ time
            futureWeatherListBinding.futureWeatherTempature.text = tempature
            if ( code.equals("0")){
                futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.sunny)
                futureWeatherListBinding.futureRVAnimation.playAnimation()
            }



        }
    }


   /* inner class WeatherViewHolder(futureWeatherListBinding: FutureWeathersRvBinding) :
        RecyclerView.ViewHolder(futureWeatherListBinding.root)*/
}