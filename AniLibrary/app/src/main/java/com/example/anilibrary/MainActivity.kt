package com.example.anilibrary

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anilibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private var isLogin: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.addOnDestinationChangedListener{_, destination, _->
            navView.isVisible = destination.id != R.id.navigation_detail &&
                    destination.id != R.id.navigation_all_list &&
                    destination.id != R.id.navigation_planned_list &&
                    destination.id != R.id.navigation_watched_list &&
                    destination.id != R.id.navigation_login &&
                    destination.id != R.id.navigation_register
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (isLogin) {
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            navGraph.setStartDestination(R.id.navigation_login)
            navController.graph = navGraph
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp()
    }

}