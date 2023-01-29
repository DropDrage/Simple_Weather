package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.city_list.presentation.R
import com.dropdrage.simpleweather.city_list.presentation.databinding.FragmentCityListBinding
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityListFragment : Fragment(R.layout.fragment_city_list) {

    private val binding by viewBinding(FragmentCityListBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.apply {
            setContent {
                ComposeMaterial3Theme {
                    CityListScreen {
                        findNavController().navigate(CityListFragmentDirections.navigateCitySearch())
                    }
                }
            }
        }
    }

}
