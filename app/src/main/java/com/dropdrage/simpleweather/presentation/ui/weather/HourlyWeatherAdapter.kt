package com.dropdrage.simpleweather.presentation.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import kotlin.properties.Delegates

class HourlyWeatherAdapter : RecyclerView.Adapter<HourWeatherViewHolder>() {
    var values: List<ViewHourWeather> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder =
        HourWeatherViewHolder(ItemHourWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HourWeatherViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size
}