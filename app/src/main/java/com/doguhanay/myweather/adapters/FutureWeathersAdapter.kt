package com.doguhanay.myweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.data.Hourly
import com.doguhanay.myweather.databinding.FutureWeathersRvBinding

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
            when (code.toInt()) {
                0,1 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.sunny)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                45,48 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.fog)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()            }
                51,53,55,56,57,61,63,65,66,67,80,81,82 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.rain)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                71,73,75,77,85,86 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.snow)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                96,99 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.lightning)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                3 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.cloudy)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                2 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.sun_cloudy)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }
                95 -> {
                    futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.windy)
                    futureWeatherListBinding.futureRVAnimation.playAnimation()
                }

            }
           /* if ( code.equals("0")){
                futureWeatherListBinding.futureRVAnimation.setAnimation(R.raw.sunny)
                futureWeatherListBinding.futureRVAnimation.playAnimation()
            }
*/


        }
    }

}