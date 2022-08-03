package com.cmpt362.regathering.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * Allows users to register using the Firebase service.
 */
class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root);

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButtonId.setOnClickListener {
            val firstName = binding.firstNameEditTextId.text.toString()
            val lastName = binding.lastNameEditTextId.text.toString()
            val email = binding.emailEditTextId.text.toString()
            val password = binding.passwordEditTextId.text.toString()

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