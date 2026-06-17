package com.petryniy1.budgetpilot.presentation.feature.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.presentation.feature.onboarding.OnboardingPreferences

class GuidedTutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                GuidedTutorialScreen(
                    onFinish = {
                        OnboardingPreferences(requireContext()).markCompleted()

                        findNavController().navigate(
                            R.id.navigationAccountsFragment,
                            null,
                            navOptions {
                                popUpTo(R.id.guidedTutorialFragment) {
                                    inclusive = true
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}
