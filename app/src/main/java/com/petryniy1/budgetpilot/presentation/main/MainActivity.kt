package com.petryniy1.budgetpilot.presentation.main

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
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
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val exitDuration = 220L
            val exitInterpolator = DecelerateInterpolator()

            splashScreenView.iconView.animate()
                .scaleX(1.08f)
                .scaleY(1.08f)
                .setDuration(exitDuration)
                .setInterpolator(exitInterpolator)
                .start()

            splashScreenView.view.animate()
                .alpha(0f)
                .setDuration(exitDuration)
                .setInterpolator(exitInterpolator)
                .withEndAction {
                    splashScreenView.remove()
                }
                .start()
        }

        initBottomMenu(savedInstanceState)
    }

    private fun initBottomMenu(savedInstanceState: Bundle?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)

        if (savedInstanceState == null) {
            val navGraph = navController.navInflater.inflate(R.navigation.main_graph)

            if (OnboardingPreferences(this).isCompleted()) {
                navGraph.setStartDestination(R.id.navigationAccountsFragment)
            }

            navController.graph = navGraph
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.isVisible =
                destination.id != R.id.onboardingFragment &&
                        destination.id != R.id.guidedTutorialFragment
        }

        bottomNavigationView.setupWithNavController(navController)
    }
}
