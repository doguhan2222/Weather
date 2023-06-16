package com.doguhanay.myweather.data

interface RecyclerViewClickListener {
    fun onItemClick(latFromAdapter: Double,longFromAdapter:Double,cityName:String)
    fun onItemClickFav(latFromAdapter: Double,longFromAdapter: Double,cityName: String)
}