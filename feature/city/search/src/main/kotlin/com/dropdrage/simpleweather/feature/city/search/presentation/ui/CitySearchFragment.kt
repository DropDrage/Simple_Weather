package com.dropdrage.simpleweather.feature.city.search.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.common.presentation.utils.ChangeableAppBar
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.common.presentation.utils.focusEditText
import com.dropdrage.simpleweather.feature.city.search.R
import com.dropdrage.simpleweather.feature.city.search.databinding.FragmentCitySearchBinding
import com.dropdrage.util.extension.implicitAccess
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

    private fun initCities() = binding.cities.implicitAccess {
        setLinearLayoutManager()
        adapter = CitySearchResultAdapter { viewModel.addCity(it) }.also { citySearchAdapter = it }

        setHasFixedSize(true)
    }

    private fun observeViews() = binding.implicitAccess {
        queryInput.addTextChangedListener {
            viewModel.updateQuery(it.toString())
        }
    }

    private fun observeViewModel() = viewModel.implicitAccess {
        collectWithViewLifecycle(searchResults, citySearchAdapter::submitList)
        collectWithViewLifecycle(cityAddedEvent) { findNavController().navigateUp() }
    }


    override fun onDestroyView() {
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
        super.onDestroyView()
    }

}
