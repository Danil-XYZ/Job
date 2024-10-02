package com.example.job

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.job.databinding.ActivityMainBinding
import com.example.job.viewModel.SharedViewModel
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() = with(binding) {
        viewModel = ViewModelProvider(this@MainActivity).get(SharedViewModel::class.java)
        viewModel.fetchDataFromServer()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.menu.forEach {
            it.isEnabled = false
            it.isCheckable = false
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.searchFragment) {
                bottomNavigationView.menu.forEach {
                    it.isEnabled = true
                    it.isCheckable = true
                }
            }
        }

        MapKitFactory.setApiKey("8fcd0a2e-b257-4c35-b5c0-b0dd1a6fbb91")
    }
}