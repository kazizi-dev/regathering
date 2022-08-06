package com.cmpt362.regathering.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.databinding.ActivityRegisterBinding
import com.cmpt362.regathering.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Allows users to register using the Firebase service.
 */
class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var interests: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root);

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButtonId.setOnClickListener {
            firstName = binding.firstNameEditTextId.text.toString()
            lastName = binding.lastNameEditTextId.text.toString()
            email = binding.emailEditTextId.text.toString()
            password = binding.passwordEditTextId.text.toString()
            val allInterests = binding.interestsEditTextId.text.toString()
            interests = ArrayList()
            interests.addAll(allInterests.split(", "))
            register(email, password, firstName, lastName)
        }

        // go to the login page if the user has already sign up
        binding.backToLoginButtonId.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    private fun register(email:String, password:String, firstName:String, lastName:String){
        // make sure there is user input
        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    val db = Firebase.firestore
                    val rootRef = FirebaseFirestore.getInstance()
                    val newUser = User()
                    newUser.email = email
                    newUser.firstName = firstName
                    newUser.lastName = lastName
                    newUser.interests = interests
                    db.collection("users").document(it.result.user!!.uid).set(newUser).addOnSuccessListener {
                            it ->
                        // Show a notification or something indicating the event was created
                        Toast.makeText(this, "Entry inserted", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(this, LoginActivity::class.java)

                    // send user first name and last name to login page
                    intent.putExtra("FIRST_NAME", firstName)
                    intent.putExtra("LAST_NAME", lastName)

                    startActivity(intent)
                }
                else {
                    Toast.makeText(this,
                        it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
        else {
            Toast.makeText(this,
                "Please complete all fields.", Toast.LENGTH_SHORT).show()
        }
    }
}