package com.dropdrage.simpleweather.presentation.ui.city.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dropdrage.simpleweather.databinding.ItemCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.util.adapter.DifferRecyclerAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.ItemsMovable
import java.util.*

class CityCurrentWeatherAdapter(
    private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit,
    private val requestDrag: (ViewHolder) -> Unit,
) : DifferRecyclerAdapter<ViewCityCurrentWeather, CityCurrentWeatherViewHolder>(CityCurrentWeatherDifferCallback()),
    ItemsMovable {

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


    private class CityCurrentWeatherDifferCallback : DiffUtil.ItemCallback<ViewCityCurrentWeather>() {
        override fun areItemsTheSame(oldItem: ViewCityCurrentWeather, newItem: ViewCityCurrentWeather): Boolean =
            oldItem.isSame(newItem)

        override fun areContentsTheSame(oldItem: ViewCityCurrentWeather, newItem: ViewCityCurrentWeather): Boolean =
            oldItem == newItem
    }
}