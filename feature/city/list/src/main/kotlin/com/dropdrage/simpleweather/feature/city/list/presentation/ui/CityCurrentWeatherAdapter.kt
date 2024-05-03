package com.dropdrage.simpleweather.feature.city.list.presentation.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.feature.city.list.databinding.ItemCityBinding
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.utils.ItemsMovable
import java.util.Collections

private const val TAG = "CityCurrentWeather"

internal class CityCurrentWeatherAdapter(
    private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit,
    private val requestDrag: (RecyclerView.ViewHolder) -> Unit,
) : DifferRecyclerAdapter<ViewCityCurrentWeather, CityCurrentWeatherViewHolder>(CityCurrentWeatherDifferCallback()),
    ItemsMovable {

    val cities: List<ViewCityCurrentWeather>
        get() = currentList


    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CityCurrentWeatherViewHolder(
        ItemCityBinding.inflate(inflater, parent, false),
        onDeleteClicked,
        requestDrag
    )

    override fun moveItem(from: Int, to: Int) {
        val list = currentList.toMutableList()
        Collections.swap(list, from, to)
        submitList(list)
    }


    override fun onBindViewHolder(holder: CityCurrentWeatherViewHolder, position: Int, payloads: MutableList<Any?>) {
        if (payloads.isEmpty() || payloads.first() !is ViewCurrentWeather) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val firstPayload = payloads.first()
            if (firstPayload is ViewCurrentWeather) {
                holder.changeWeather(firstPayload)
            } else {
                Log.w(TAG, "Unknown payload ${firstPayload?.javaClass?.name}")
            }
        }
    }
}
