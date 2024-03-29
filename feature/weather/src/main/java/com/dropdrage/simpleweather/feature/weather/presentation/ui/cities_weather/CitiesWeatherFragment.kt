package com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.utils.ChangeableAppBar
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.databinding.FragmentCitiesWeatherBinding
import com.dropdrage.util.extension.implicitAccess
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesWeatherFragment : Fragment(R.layout.fragment_cities_weather) {

    private val binding by viewBinding(FragmentCitiesWeatherBinding::bind)
    private val viewModel: CitiesWeatherViewModel by viewModels()
    private val observableCityTitle: ObservableCityTitle by viewModels<CitiesSharedViewModel>()

    private lateinit var citiesAdapter: CitiesWeatherAdapter
    private var tabLayoutMediator: TabLayoutMediator? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCitiesWeatherPager()
        observeViewModel()
        observeCityTitle()

        val activity = requireActivity()
        (activity as ChangeableAppBar).changeAppBar(binding.toolbar)
        activity.addMenuProvider(MainMenuProvider(), viewLifecycleOwner)
    }

    private fun initCitiesWeatherPager() {
        binding.citiesWeather.adapter = CitiesWeatherAdapter(this).also { citiesAdapter = it }

        val titleMarginEnd = resources.getDimensionPixelOffset(R.dimen.toolbar_expanded_margin_end)
        var previousScrollPercent = 0f
        binding.appBar.addOnOffsetChangedListener { appBar, verticalOffset ->
            val totalScrollRange = appBar.totalScrollRange
            val scrollPercentDone = (totalScrollRange + verticalOffset).toFloat() / totalScrollRange

            if (scrollPercentDone != previousScrollPercent) {
                binding.tabs.alpha = scrollPercentDone
                val toolbarMarginPercent = scrollPercentDone * scrollPercentDone * scrollPercentDone * scrollPercentDone
                binding.collapsingToolbar.expandedTitleMarginEnd = (titleMarginEnd * toolbarMarginPercent).toInt()

                previousScrollPercent = scrollPercentDone
            }
        }

        updateTabLayout()
    }

    private fun observeViewModel() = viewModel.implicitAccess {
        collectWithViewLifecycle(cities) {
            citiesAdapter.citiesCount = it.size
            updateTabLayout()
        }
    }

    private fun updateTabLayout() {
        tabLayoutMediator?.detach()
        TabLayoutMediator(binding.tabs, binding.citiesWeather) { _, _ -> }.also { tabLayoutMediator = it }.attach()
    }

    private fun observeCityTitle() = collectWithViewLifecycle(observableCityTitle.currentCityTitle) {
        val context = requireContext()
        binding.collapsingToolbar.implicitAccess {
            title = it.city.getMessage(context)
            subtitle = it.countryCode.getMessage(context)
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.updateWeather()
    }

    override fun onDestroyView() {
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
        tabLayoutMediator = null
        super.onDestroyView()
    }


    @Suppress("RedundantInnerClassModifier")
    private inner class MainMenuProvider : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            R.id.addCity -> {
                findNavController().navigate(CitiesWeatherFragmentDirections.navigateCityList())
                true
            }
            R.id.settings -> {
                findNavController().navigate(CitiesWeatherFragmentDirections.navigateSettingsFragment())
                true
            }
            else -> false
        }

    }

}
