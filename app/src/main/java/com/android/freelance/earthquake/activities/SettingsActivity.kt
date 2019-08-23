package com.android.freelance.earthquake.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import com.android.freelance.earthquake.R

class SettingsActivity : AppCompatActivity() {

    private val LOG_TAG = SettingsActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(LOG_TAG, "TEST: onCreate() called...")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    class EarthquakePreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        private val LOG_TAG = EarthquakePreferenceFragment::class.java.name

        override fun onCreate(savedInstanceState: Bundle?) {
            Log.i(LOG_TAG, "TEST: onCreate() in EarthquakePreferenceFragment class is called...")

            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.main_settings)

            val minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key))
            bindPreferenceSummaryToValue(minMagnitude)

            val orderBy = findPreference(getString(R.string.settings_order_by_key))
            bindPreferenceSummaryToValue(orderBy)
        }

        override fun onPreferenceChange(preference: Preference, value: Any): Boolean {
            Log.i(LOG_TAG, "TEST: onPreferenceChange() in EarthquakePreferenceFragment class is called...")

            val stringValue = value.toString()

            if (preference is ListPreference) {
                val prefIndex = preference.findIndexOfValue(stringValue)
                if (prefIndex >= 0) {
                    val labels = preference.entries
                    preference.setSummary(labels[prefIndex])
                }
            } else {
                preference.summary = stringValue
            }
            return true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            Log.i(LOG_TAG, "TEST: EarthquakePreferenceFragment bindPreferenceSummaryToValue() called...")

            preference.onPreferenceChangeListener = this
            val preferences = PreferenceManager.getDefaultSharedPreferences(preference.context)
            val preferenceString = preferences.getString(preference.key, "")
            onPreferenceChange(preference, preferenceString!!)
        }
    }
}
