package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemDayWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.differ.DifferRecyclerAdapter

class DailyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewDayWeather>) :
    DifferRecyclerAdapter<ViewDayWeather, DayWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = DayWeatherViewHolder(
        ItemDayWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}