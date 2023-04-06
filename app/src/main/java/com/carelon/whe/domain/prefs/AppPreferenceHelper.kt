package com.carelon.whe.domain.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.carelon.whe.constants.PreferenceKeyConstants
import com.carelon.whe.util.TackMedicationStatus
import com.carelon.whe.util.TrackYourStepsStatus
import javax.inject.Inject

class AppPreferenceHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {


    var isInstalled: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_APP_INSTALLED, true)
        set(value) {
            sharedPreferences.edit { putBoolean(PreferenceKeyConstants.IS_APP_INSTALLED, value) }
        }

    var isPrescriptionRead: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_NEW_PRESCRIPTION_READ, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(
                    PreferenceKeyConstants.IS_NEW_PRESCRIPTION_READ,
                    value
                )
            }
        }

    var savedMedication: String?
        get() = sharedPreferences.getString(PreferenceKeyConstants.SAVED_MEDICATION, "[]")
        set(value) {
            sharedPreferences.edit { putString(PreferenceKeyConstants.SAVED_MEDICATION, value) }
        }

    var accessToken: String?
        get() = sharedPreferences.getString(PreferenceKeyConstants.ACCESS_TOKEN, "")
        set(value) {
            sharedPreferences.edit { putString(PreferenceKeyConstants.ACCESS_TOKEN, value) }
        }

    var isFeaturePageShown: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_FEATURE_PAGE_SHOWN, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(
                    PreferenceKeyConstants.IS_FEATURE_PAGE_SHOWN,
                    value
                )
            }
        }

    var isEarlyAccessPageShown: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_EARLY_ACCESS_SHOWN, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(
                    PreferenceKeyConstants.IS_EARLY_ACCESS_SHOWN,
                    value
                )
            }
        }

    var isPrivacyPolicyPageShown: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_PRIVACY_POLICY_SHOWN, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(
                    PreferenceKeyConstants.IS_PRIVACY_POLICY_SHOWN,
                    value
                )
            }
        }

    var isEmailVerified: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.IS_EMAIL_VERIFIED, false)
        set(value) {
            sharedPreferences.edit { putBoolean(PreferenceKeyConstants.IS_EMAIL_VERIFIED, value) }
        }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    var showWelcomeDialog: Boolean
        get() = sharedPreferences.getBoolean(PreferenceKeyConstants.KEY_SHOW_WELCOME_DIALOG, true)
        set(value) {
            sharedPreferences.edit {
                putBoolean(
                    PreferenceKeyConstants.KEY_SHOW_WELCOME_DIALOG,
                    value
                )
            }
        }

    var dailyCheckInTime: String
        get() = sharedPreferences.getString(PreferenceKeyConstants.KEY_DAILY_CHECK_IN, "") ?: ""
        set(value) {
            sharedPreferences.edit { putString(PreferenceKeyConstants.KEY_DAILY_CHECK_IN, value) }
        }

    var savedTrackStepActivity: Int
        get() = sharedPreferences.getInt(PreferenceKeyConstants.IS_TRACK_STEP_ACTIVITY_STEP_COMPLETED, TrackYourStepsStatus.TRACK_YOUR_STEPS_NOT_STARTED.value)
        set(value) {
            sharedPreferences.edit { putInt(PreferenceKeyConstants.IS_TRACK_STEP_ACTIVITY_STEP_COMPLETED, value) }
        }

    var isTrackMedsCompleted: Int
        get() = sharedPreferences.getInt(PreferenceKeyConstants.TRACK_MEDICINE_STATUS, TackMedicationStatus.MEDICATION_NOT_CHOOSE_AS_FOCUS_AREA.value)
        set(value) {
            sharedPreferences.edit { putInt(PreferenceKeyConstants.TRACK_MEDICINE_STATUS, value) }
        }
}