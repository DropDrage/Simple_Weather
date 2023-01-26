package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.ui.TitledFragment
import com.dropdrage.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.city_list.presentation.R
import com.dropdrage.simpleweather.city_list.presentation.databinding.FragmentCityListBinding
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityListFragment : TitledFragment(R.layout.fragment_city_list, R.string.city_list_title) {

    private val binding by viewBinding(FragmentCityListBinding::bind)
    private val viewModel: CityListViewModel by viewModels()

    private lateinit var cityCurrentWeatherAdapter: CityCurrentWeatherAdapter

    @Inject
    internal lateinit var router: Router


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCityList()
        setOnClickListeners()
        observeViewModel()
    }

    private fun setOnClickListeners() = binding.apply {
        addCity.setOnClickListener { router.navigateTo(Screens.CitySearch()) }
    }

    private fun initCityList() = binding.cities.apply {
        val itemTouchHelper = ItemTouchHelper(CityCurrentWeatherDragCallback {
            viewModel.saveOrder(cityCurrentWeatherAdapter.cities)
        })
        itemTouchHelper.attachToRecyclerView(this)

        setLinearLayoutManager()
        adapter = CityCurrentWeatherAdapter(viewModel::deleteCity, itemTouchHelper::startDrag)
            .apply { addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)) }
            .also { cityCurrentWeatherAdapter = it }
    }

    private fun observeViewModel() = viewModel.apply {
        collectWithViewLifecycle(citiesCurrentWeathers, cityCurrentWeatherAdapter::submitList)
    }

}
