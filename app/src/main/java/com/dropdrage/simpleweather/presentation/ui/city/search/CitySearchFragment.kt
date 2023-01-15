package com.dropdrage.simpleweather.presentation.ui.city.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.simpleweather.databinding.FragmentCitySearchBinding
import com.dropdrage.simpleweather.presentation.ui.ChangeableAppBar
import com.dropdrage.simpleweather.presentation.util.extension.collectWithViewLifecycle
import com.dropdrage.simpleweather.presentation.util.extension.focusEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CitySearchFragment : Fragment(R.layout.fragment_city_search) {

    private val binding by viewBinding(FragmentCitySearchBinding::bind)
    private val viewModel: CitySearchViewModel by viewModels()

    private lateinit var citySearchAdapter: CitySearchResultAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as ChangeableAppBar).changeAppBar(binding.toolbar)

        initCities()
        observeViews()
        observeViewModel()
        focusEditText(binding.queryInput)
    }

    private fun initCities() = binding.cities.apply {
        setLinearLayoutManager()
        adapter = CitySearchResultAdapter { viewModel.addCity(it) }.also { citySearchAdapter = it }

        setHasFixedSize(true)
    }

    private fun observeViews() = binding.apply {
        queryInput.addTextChangedListener {
            viewModel.updateQuery(it.toString())
        }
    }

    private fun observeViewModel() = viewModel.apply {
        collectWithViewLifecycle(searchResults, citySearchAdapter::submitList)
        collectWithViewLifecycle(cityAddedEvent, { findNavController().navigateUp() })
    }


    override fun onDestroyView() {
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
        super.onDestroyView()
    }

}