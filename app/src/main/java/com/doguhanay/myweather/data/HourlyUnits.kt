package com.doguhanay.myweather.data

import com.google.gson.annotations.SerializedName


data class HourlyUnits (

  @SerializedName("time"           ) var time          : String? = null,
  @SerializedName("temperature_2m" ) var temperature2m : String? = null,
  @SerializedName("weathercode"    ) var weathercode   : String? = null

)