package com.dropdrage.simpleweather.weather.presentation.ui.city.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.weather.presentation.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.weather.presentation.model.ViewHourWeather

internal class HourlyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewHourWeather>) :
    DifferRecyclerAdapter<ViewHourWeather, HourWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = HourWeatherViewHolder(
        ItemHourWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}