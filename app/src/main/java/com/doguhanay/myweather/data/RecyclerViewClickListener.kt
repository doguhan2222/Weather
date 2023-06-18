package com.doguhanay.myweather.data

//For handle recyclerview item click
interface RecyclerViewClickListener {
    fun onItemClick(latFromAdapter: Double,longFromAdapter:Double,cityName:String)
    fun onItemClickFav(latFromAdapter: Double,longFromAdapter: Double,cityName: String)
}