package com.cmpt362.regathering.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cmpt362.regathering.R
import com.cmpt362.regathering.activity.LoginActivity
import com.cmpt362.regathering.activity.ProfileActivity
import com.cmpt362.regathering.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
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
        val key = preference.key
        if(key == "settings_profile"){
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
        else if(key == "interests_preference"){
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Interests")

            val input = EditText(requireContext())
            input.inputType = InputType.TYPE_CLASS_TEXT

            val userId: String = firebaseAuth.currentUser?.uid.toString()
            var userInterests: ArrayList<String>

            // read user interests from database
            firestore.collection("users").document(userId).get().addOnSuccessListener {
                val user = it.toObject<User>()!!
                userInterests = user.interests
                for(i in 0 until userInterests.size){
                    if(i == 0){
                        input.setText(userInterests[0])
                    }
                    else{
                        val interests = input.text.toString() + "," + userInterests[i]
                        input.setText(interests)
                    }
                }
            }

            // update dialog view with user input
            builder.setView(input)

            // save updated interests
            builder.setPositiveButton("save") { dialog, which ->
                userInterests = ArrayList<String>()
                userInterests.addAll(input.text.split(","))

                // save interests in the database
                firestore.collection("users").document(userId)
                    .update("interests", userInterests)
            }
            builder.setNegativeButton("cancel"){dialog, which -> }
            builder.show()
        }
        else if(key == "user_logout"){
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        return false

    }
}