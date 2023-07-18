package com.example.githubuser

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.setting.SettingViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel : SettingViewModel by viewModels { factory }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        viewModel.getTheme.observe(this){dark ->
            AppCompatDelegate.setDefaultNightMode(if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.home_navigation -> visible()
                R.id.favorite_navigation -> visible()
                R.id.setting_navigation -> visible()
                else -> invisible()
            }
        }
    }

    private fun invisible() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun visible() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}