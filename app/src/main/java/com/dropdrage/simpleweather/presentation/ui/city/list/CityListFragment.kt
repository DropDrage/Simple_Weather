package com.dropdrage.simpleweather.presentation.ui.city.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCityListBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toImmutableList

@AndroidEntryPoint
class CityListFragment : Fragment(R.layout.fragment_city_list) {

    private val binding by viewBinding(FragmentCityListBinding::bind)
    private val viewModel: CityListViewModel by viewModels()

    private lateinit var cityCurrentWeatherAdapter: CityCurrentWeatherAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCityList()
        observeViewModel()

        binding.button.setOnClickListener {
            findNavController().navigate(CityListFragmentDirections.navigateCitySearch())
        }
    }

    private fun initCityList() = binding.cities.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = CityCurrentWeatherAdapter(viewModel::deleteCity)
            .also { cityCurrentWeatherAdapter = it }

        val itemTouchHelper = ItemTouchHelper(ItemDragCallback {
            viewModel.changeOrder(cityCurrentWeatherAdapter.cities)
        })
        itemTouchHelper.attachToRecyclerView(this)
    }

    private fun observeViewModel() = viewModel.apply {
        citiesCurrentWeathers.observe(this@CityListFragment) {
            cityCurrentWeatherAdapter.submitValues(it.toImmutableList())
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadCities()
    }

}