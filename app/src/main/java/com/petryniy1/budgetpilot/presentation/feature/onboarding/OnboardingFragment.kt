package com.petryniy1.budgetpilot.presentation.feature.onboarding

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

class OnboardingFragment : Fragment() {

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
                OnboardingScreen(
                    onFinish = {
                        findNavController().navigate(
                            R.id.guidedTutorialFragment,
                            null,
                            navOptions {
                                popUpTo(R.id.onboardingFragment) {
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
