package com.dropdrage.simpleweather.city_search.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.utils.ChangeableAppBar
import com.dropdrage.simpleweather.city_search.presentation.R
import com.dropdrage.simpleweather.city_search.presentation.databinding.FragmentCitySearchBinding
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySearchFragment : Fragment(R.layout.fragment_city_search) {

    private val binding by viewBinding(FragmentCitySearchBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as ChangeableAppBar).hideAppBar()

        binding.content.setContent {
            ComposeMaterial3Theme {
                CitySearchScreen {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
        super.onDestroyView()
    }

}
