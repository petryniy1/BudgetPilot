package com.petryniy1.budgetpilot.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.presentation.feature.onboarding.OnboardingPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        
        super.onCreate(savedInstanceState)

        initBottomMenu()
    }

    private fun initBottomMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.main_graph)

        if (OnboardingPreferences(this).isCompleted()) {
            navGraph.setStartDestination(R.id.navigationOperationsFragment)
        }

        navController.graph = navGraph
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.isVisible = destination.id != R.id.onboardingFragment
        }

        bottomNavigationView.setupWithNavController(navController)
    }
}
