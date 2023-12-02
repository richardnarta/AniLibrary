package com.example.anilibrary

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anilibrary.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var sp: SharedPreferences

    private val auth: FirebaseAuth = AniLibrary.auth

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val user = auth.currentUser
        sp = applicationContext.getSharedPreferences("AppData", Context.MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.addOnDestinationChangedListener{_, destination, _->
            navView.isVisible = destination.id != R.id.navigation_detail &&
                    destination.id != R.id.navigation_all_list &&
                    destination.id != R.id.navigation_planned_list &&
                    destination.id != R.id.navigation_watched_list &&
                    destination.id != R.id.navigation_login &&
                    destination.id != R.id.navigation_register&&
                    destination.id != R.id.navigation_onBoarding &&
                    destination.id != R.id.navigation_edit_profile &&
                    destination.id != R.id.navigation_change_password &&
                    destination.id != R.id.navigation_change_email
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (user == null) {
            lifecycleScope.launch {
                supportActionBar?.setShowHideAnimationEnabled(false)
                supportActionBar?.hide()
            }
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            if (sp.getBoolean("isFirstOpen", true)) {
                val editor: SharedPreferences.Editor = sp.edit()
                editor.putBoolean("isFirstOpen", false)
                editor.apply()
                navGraph.setStartDestination(R.id.navigation_onBoarding)
            } else {
                navGraph.setStartDestination(R.id.navigation_login)
            }
            navController.graph = navGraph
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp()
    }

}