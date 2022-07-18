package com.cmpt362.regathering

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * FragmentSettings class that extends PreferenceFragmentCompat class
 * used to beautifully display settings tab when user chooses
 * to open Settings tab in the navigation bar
 */
class FragmentSettings: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    /**
     * Method triggered when user clicks any setting on the settings fragment
     */
    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        val key = preference!!.key
        if(key == "settings_profile"){
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
        else if(key == "webpage"){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(preference.summary as String?)
            startActivity(intent)
        }
        return true
    }
}