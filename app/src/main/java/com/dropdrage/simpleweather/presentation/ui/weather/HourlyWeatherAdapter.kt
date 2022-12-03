package com.dropdrage.simpleweather.presentation.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleRecyclerListAdapter

class HourlyWeatherAdapter : SimpleRecyclerListAdapter<ViewHourWeather, HourWeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder =
        HourWeatherViewHolder(ItemHourWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}