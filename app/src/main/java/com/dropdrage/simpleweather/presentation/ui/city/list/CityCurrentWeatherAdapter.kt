package com.dropdrage.simpleweather.presentation.ui.city.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dropdrage.simpleweather.databinding.ItemCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.presentation.util.adapter.ItemsMovable
import com.dropdrage.simpleweather.presentation.util.adapter.differ.DifferRecyclerAdapter
import java.util.*

class CityCurrentWeatherAdapter(
    private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit,
    private val requestDrag: (ViewHolder) -> Unit,
) : DifferRecyclerAdapter<ViewCityCurrentWeather, CityCurrentWeatherViewHolder>(), ItemsMovable {

    val cities: List<ViewCityCurrentWeather>
        get() = differ.currentList

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CityCurrentWeatherViewHolder(
        ItemCityBinding.inflate(inflater, parent, false),
        onDeleteClicked,
        requestDrag
    )

    override fun moveItem(from: Int, to: Int) {
        val list = differ.currentList.toMutableList()
        Collections.swap(list, from, to)
        submitValues(list)
    }


    override fun onBindViewHolder(holder: CityCurrentWeatherViewHolder, position: Int, payloads: MutableList<Any?>) {
        if (payloads.isEmpty() || payloads[0] !is ViewCurrentWeather) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val firstPayload = payloads.first()
            if (firstPayload is ViewCurrentWeather) {
                holder.changeWeather(firstPayload)
            } else {
                Log.w("CityCurrentWeather", "Unknown payload ${firstPayload?.javaClass?.name}")
            }
        }
    }
}