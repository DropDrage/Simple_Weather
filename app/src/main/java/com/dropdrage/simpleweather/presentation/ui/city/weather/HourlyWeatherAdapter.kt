package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.simple.SimpleRecyclerListAdapter

class HourlyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewHourWeather>) :
    SimpleRecyclerListAdapter<ViewHourWeather, HourWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = HourWeatherViewHolder(
        ItemHourWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}