package com.dropdrage.simpleweather.presentation.ui.city.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCitySearchBinding
import com.dropdrage.simpleweather.presentation.ui.ChangeableAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySearchFragment : Fragment(R.layout.fragment_city_search) {

    private val binding by viewBinding(FragmentCitySearchBinding::bind)
    private val viewModel: CitySearchViewModel by viewModels()

    private lateinit var citiesAdapter: CitiesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as ChangeableAppBar).changeAppBar(binding.toolbar)

        initCities()
        observeViews()
        observeViewModel()
    }

    private fun initCities() = binding.cities.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = CitiesAdapter { viewModel.addCity(it) }.also { citiesAdapter = it }
    }

    private fun observeViews() = binding.apply {
        queryInput.addTextChangedListener {
            viewModel.updateQuery(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
    }

    private fun observeViewModel() = viewModel.apply {
        searchResults.observe(viewLifecycleOwner) {
            citiesAdapter.submitValues(it)
        }
        cityAddedEvent.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

}