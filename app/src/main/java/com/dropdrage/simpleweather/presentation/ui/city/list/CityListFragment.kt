package com.dropdrage.simpleweather.presentation.ui.city.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
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
        setOnClickListeners()
        observeViewModel()
    }

    private fun setOnClickListeners() = binding.apply {
        addCity.setOnClickListener {
            findNavController().navigate(CityListFragmentDirections.navigateCitySearch())
        }
    }

    private fun initCityList() = binding.cities.apply {
        val itemTouchHelper = ItemTouchHelper(CityCurrentWeatherDragCallback {
            viewModel.changeOrder(cityCurrentWeatherAdapter.cities)
        })
        itemTouchHelper.attachToRecyclerView(this)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = CityCurrentWeatherAdapter(viewModel::deleteCity, itemTouchHelper::startDrag)
            .apply { addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)) }
            .also { cityCurrentWeatherAdapter = it }
    }

    private fun observeViewModel() = viewModel.apply {
        citiesCurrentWeathers.observe(viewLifecycleOwner) {
            cityCurrentWeatherAdapter.submitValues(it.toImmutableList())
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadCities()
    }

}