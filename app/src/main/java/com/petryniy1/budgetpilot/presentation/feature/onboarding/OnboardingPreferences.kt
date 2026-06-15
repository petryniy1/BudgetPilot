package com.petryniy1.budgetpilot.presentation.feature.onboarding

import android.content.Context

class OnboardingPreferences(
    context: Context
) {
    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun isCompleted(): Boolean {
        return preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun markCompleted() {
        preferences.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, true)
            .apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "budget_pilot_onboarding"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
