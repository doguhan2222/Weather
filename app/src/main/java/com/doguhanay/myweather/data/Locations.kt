package com.doguhanay.myweather.data

import com.google.gson.annotations.SerializedName


data class Locations (

  @SerializedName("results"           ) var results          : ArrayList<LocationsResult> = arrayListOf(),
  @SerializedName("generationtime_ms" ) var generationtimeMs : Double?            = null

)