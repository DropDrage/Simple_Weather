package com.dropdrage.simpleweather.presentation.ui.cities_weather

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
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCitiesWeatherBinding
import com.dropdrage.simpleweather.presentation.ui.ChangeableAppBar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.pow

@AndroidEntryPoint
class CitiesWeatherFragment : Fragment(R.layout.fragment_cities_weather), TitleHolder {

    private val binding by viewBinding(FragmentCitiesWeatherBinding::bind)
    private val viewModel: CitiesWeatherViewModel by viewModels()

    private lateinit var citiesAdapter: CitiesWeatherAdapter
    private var tabLayoutMediator: TabLayoutMediator? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCitiesWeatherPager()
        observeViewModel()

        requireActivity().addMenuProvider(MainMenuProvider(), viewLifecycleOwner)
        viewModel.loadCities()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as ChangeableAppBar).changeAppBar(binding.toolbar)
    }

    private fun initCitiesWeatherPager() {
        binding.citiesWeather.adapter = CitiesWeatherAdapter(childFragmentManager, lifecycle)
            .also { citiesAdapter = it }

        val titleMarginEnd = resources.getDimensionPixelOffset(R.dimen.toolbar_expanded_margin_end)
        var previousScrollPercent = 0f
        binding.appBar.addOnOffsetChangedListener { appBar, verticalOffset ->
            val totalScrollRange = appBar.totalScrollRange
            val scrollPercentDone = (totalScrollRange + verticalOffset).toFloat() / totalScrollRange

            if (scrollPercentDone != previousScrollPercent) {
                binding.tabs.alpha = scrollPercentDone
                val toolbarMarginPercent = pow(scrollPercentDone.toDouble(), 4.0)
                binding.collapsingToolbar.expandedTitleMarginEnd = (titleMarginEnd * toolbarMarginPercent).toInt()

                previousScrollPercent = scrollPercentDone
            }
        }

        updateTabLayout()
    }

    private fun observeViewModel() = viewModel.apply {
        cities.observe(viewLifecycleOwner) {
            citiesAdapter.citiesCount = it.size
            updateTabLayout()
        }
    }

    private fun updateTabLayout() {
        tabLayoutMediator?.detach()
        TabLayoutMediator(binding.tabs, binding.citiesWeather) { _, _ -> }.also { tabLayoutMediator = it }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
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

    override fun setTitle(title: String, subtitle: String) {
        binding.collapsingToolbar.title = title
        binding.collapsingToolbar.subtitle = subtitle
    }
}