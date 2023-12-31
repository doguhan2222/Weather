package com.doguhanay.myweather.data

import com.google.gson.annotations.SerializedName


data class Hourly (

  @SerializedName("time"           ) var time          : ArrayList<String> = arrayListOf(),
  @SerializedName("temperature_2m" ) var temperature2m : ArrayList<Double> = arrayListOf(),
  @SerializedName("weathercode"    ) var weathercode   : ArrayList<Int>    = arrayListOf()

)