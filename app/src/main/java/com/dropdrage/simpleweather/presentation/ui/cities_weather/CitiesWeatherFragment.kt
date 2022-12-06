package com.dropdrage.simpleweather.presentation.ui.cities_weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCitiesWeatherBinding
import com.dropdrage.simpleweather.presentation.ui.ChangeableAppBar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

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

        requireActivity().addMenuProvider(MainMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
        viewModel.loadCities()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as ChangeableAppBar).changeAppBar(binding.toolbar)
    }

    private fun initCitiesWeatherPager() {
        binding.citiesWeather.adapter = CitiesWeatherAdapter(childFragmentManager, lifecycle)
            .also { citiesAdapter = it }
        binding.appBar.addOnOffsetChangedListener { appBar, verticalOffset ->
            val totalScrollRange = appBar.totalScrollRange
            binding.tabs.alpha = (totalScrollRange + verticalOffset).toFloat() / totalScrollRange
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