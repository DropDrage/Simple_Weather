package com.dropdrage.simpleweather.presentation.ui.city.list

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.ItemCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.presentation.util.adapter.DragListener
import com.dropdrage.simpleweather.presentation.util.adapter.simple.SimpleViewHolder

private const val ON_DRAG_ALPHA = 0.5f
private const val IDLE_ALPHA = 1f

class CityCurrentWeatherViewHolder constructor(
    binding: ItemCityBinding,
    private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit,
    private val requestDrag: (RecyclerView.ViewHolder) -> Unit,
) : SimpleViewHolder<ViewCityCurrentWeather, ItemCityBinding>(binding), DragListener {

    private lateinit var deletePopup: PopupMenu

    @SuppressLint("ClickableViewAccessibility")
    override fun bindData(value: ViewCityCurrentWeather) {
        binding.apply {
            city.text = value.city.name
            countryCode.text = value.city.country.code

            changeWeather(value.currentWeather)

            drag.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    requestDrag(this@CityCurrentWeatherViewHolder)
                    true
                } else false
            }
        }

        initDeletePopup(value)
    }

    private fun initDeletePopup(value: ViewCityCurrentWeather) {
        deletePopup = PopupMenu(binding.root.context, binding.temperature).apply {
            menuInflater.inflate(R.menu.city_delete, menu)

            setOnMenuItemClickListener {
                if (it.itemId == R.id.delete) {
                    onDeleteClicked(value)
                    true
                } else false
            }
        }

        binding.root.setOnLongClickListener {
            deletePopup.show()
            true
        }
    }


    fun changeWeather(currentWeather: ViewCurrentWeather) {
        binding.apply {
            weather.setImageResource(currentWeather.weatherType.iconRes)
            weather.contentDescription = root.context.getString(currentWeather.weatherType.weatherDescriptionRes)
            temperature.text = currentWeather.temperature
        }
    }


    override fun onDragStart() {
        binding.root.alpha = ON_DRAG_ALPHA
        deletePopup.dismiss()
    }

    override fun onDragEnd() {
        binding.root.alpha = IDLE_ALPHA
    }
}