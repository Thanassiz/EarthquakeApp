package com.example.android.earthquakeapp;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /**
     * PreferenceFragment can implement the OnPreferenceChangeListener interface to get notified
     * when a preference changes. Then when a single Preference has been changed by the user and
     * is about to be saved, the onPreferenceChange() method will be invoked with the key of
     * the preference that was changed. Note that this method returns a boolean, which allows us to prevent a proposed preference change by returning false.
     */
    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            /**
             * updates the preference summary when the settings activity is launched. 
             * Given the key of a preference, we can use PreferenceFragment's findPreference() method 
             * to get the Preference object, and setup the preference using a helper method called bindPreferenceSummaryToValue().
             */
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

        }

        // helper method that sets the current EarthquakePreferenceFragment instance as the listener on each preference.
        private void bindPreferenceSummaryToValue(Preference preference) {

            /**
             * read the current value of the preference stored in the SharedPreferences on the device,
             * and display that in the preference summary (so that the user can see the current value of the preference).
             */
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);

        }

        /**
         * Since this is the first ListPreference that the EarthquakePreferenceFragment is encountering,
         * update the onPreferenceChange() method in EarthquakePreferenceFragment to properly update
         * the summary of a ListPreference (using the label, instead of the key).
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}
