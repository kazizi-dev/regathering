package com.cmpt362.regathering.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cmpt362.regathering.activity.ProfileActivity
import com.cmpt362.regathering.R
import com.cmpt362.regathering.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * FragmentSettings class that extends PreferenceFragmentCompat class
 * used to beautifully display settings tab when user chooses
 * to open Settings tab in the navigation bar
 */
class FragmentSettings: PreferenceFragmentCompat() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
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
        else if(key == "webpage"){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(preference.summary as String?)
            startActivity(intent)
        }
        else if(key == "user_logout"){
            firebaseAuth.signOut()

            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        else if(key == "interests_preference"){
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                val stringToShow = (it.get("interests") as ArrayList<*>).joinToString(separator = ", ")
                //preference.title = stringToShow
                //(preference as EditTextPreference).text = stringToShow
                (preference as EditTextPreference).dialogMessage = stringToShow
            }
            preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{preferenceHere, newValue ->
                val newString = newValue as String
                val newSplitArray = newString.split(", ")
                firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                    for(word in newSplitArray){
                        if(word.isNotEmpty()){
                            it.reference.update("interests", FieldValue.arrayUnion(word))
                        }
                    }
                }
                true
            }
        }
        return true
    }
}