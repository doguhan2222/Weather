package com.doguhanay.myweather.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doguhanay.myweather.R
import com.doguhanay.myweather.data.RecyclerViewClickListener
import com.doguhanay.myweather.databinding.FavLocationsRvBinding
import javax.inject.Inject

class FavLocationsAdapter @Inject constructor(
    private val sharedPreferences: SharedPreferences, private val testClick: RecyclerViewClickListener) :
    RecyclerView.Adapter<FavLocationsAdapter.FavViewHolder>() {

    private var favLocationList: List<String>? = null
    var lat: String? = null
    var long: String? = null
    var currentListItem: String? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavLocationsAdapter.FavViewHolder {

        val favLocationsRvBinding = DataBindingUtil.inflate<FavLocationsRvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.fav_locations_rv, parent, false
        )
        return FavViewHolder(favLocationsRvBinding)
    }

    override fun onBindViewHolder(holder: FavLocationsAdapter.FavViewHolder, position: Int) {
        currentListItem = favLocationList!![position]
        holder.bind(currentListItem!!)


        holder.itemView.setOnClickListener {
            val value: String? = sharedPreferences.getString(favLocationList!![position], "olmadı")
            lat = value?.split(",")?.get(0)
            long = value?.split(",")?.get(1)
            testClick.onItemClickFav(lat!!.toDouble(), long!!.toDouble(), favLocationList!![position])

        }


    }

    override fun getItemCount(): Int {
        return if (favLocationList != null) {
            favLocationList!!.size
        } else {
            0
        }
    }

    fun setFavLocList(list: List<String>) {
        this.favLocationList = list
        notifyDataSetChanged()
    }

    inner class FavViewHolder(private val favLocationsRvBinding: FavLocationsRvBinding) :
        RecyclerView.ViewHolder(favLocationsRvBinding.root) {
        fun bind(test: String) {
            favLocationsRvBinding.favLocButton.text = test
        }

    }
}