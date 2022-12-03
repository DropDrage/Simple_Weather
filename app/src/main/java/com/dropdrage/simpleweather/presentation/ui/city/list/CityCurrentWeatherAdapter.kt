package com.dropdrage.simpleweather.presentation.ui.city.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.util.adapter.DifferRecyclerAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.ItemsMovable
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleViewHolder
import java.util.*

class CityCurrentWeatherAdapter(private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit) :
    DifferRecyclerAdapter<ViewCityCurrentWeather, SimpleViewHolder<ViewCityCurrentWeather, *>>(), ItemsMovable {

    val cities: List<ViewCityCurrentWeather>
        get() = differ.currentList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CityCurrentWeatherViewHolder(
        ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onDeleteClicked
    )

    override fun moveItem(from: Int, to: Int) {
        val list = differ.currentList.toMutableList()
        Collections.swap(list, from, to)
        submitValues(list)
    }
}