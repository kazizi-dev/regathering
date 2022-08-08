package com.cmpt362.regathering.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cmpt362.regathering.R
import com.cmpt362.regathering.activity.LoginActivity
import com.cmpt362.regathering.activity.ProfileActivity
import com.cmpt362.regathering.adapter.EventAdapter
import com.cmpt362.regathering.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

/**
 * FragmentSettings class that extends PreferenceFragmentCompat class
 * used to beautifully display settings tab when user chooses
 * to open Settings tab in the navigation bar
 */
class FragmentSettings: PreferenceFragmentCompat() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var eventsQuery: Query

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        firestore = Firebase.firestore
    }

    /**
     * Method triggered when user clicks any setting on the settings fragment
     */
    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        val key = preference!!.key
        if(key == "settings_profile"){
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
        else if(key == "user_logout"){
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        return false

    }
}