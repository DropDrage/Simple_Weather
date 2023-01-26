package com.dropdrage.simpleweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.utils.ChangeableAppBar
import com.dropdrage.common.presentation.utils.TitledAppBar
import com.dropdrage.simpleweather.databinding.ActivityMainBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity(R.layout.activity_main), ChangeableAppBar, TitledAppBar {

    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val appNavigator = AppNavigator(this, R.id.fragmentContainer)


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        setupAppBar()
        router.newRootScreen(Screens.CitiesWeather())
    }

    private fun setupAppBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        router.exit()
        return true
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(appNavigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }


    override fun changeAppBar(toolbar: Toolbar, enableHomeButton: Boolean) {
        supportActionBar?.hide()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHomeButton)
    }

    override fun restoreDefaultAppBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.show()
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

}
