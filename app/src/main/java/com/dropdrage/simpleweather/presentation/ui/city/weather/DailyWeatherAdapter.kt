package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.databinding.ItemDayWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather

class DailyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewDayWeather>) :
    DifferRecyclerAdapter<ViewDayWeather, DayWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = DayWeatherViewHolder(
        ItemDayWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}