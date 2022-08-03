package com.cmpt362.regathering.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * Allow users to login using their email.
 */
class LoginActivity: AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);

        firebaseAuth = FirebaseAuth.getInstance()

        // go to the register page if the user has not signed up yet
        binding.loginButtonId.setOnClickListener {
            val email = binding.emailEditTextId.text.toString()
            val password = binding.passwordEditTextId.text.toString()

            login(email, password)
        }

        binding.backToRegisterButtonId.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    private fun login(email: String, password: String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    startActivity(Intent(this, StartActivity::class.java))
                }
                else {
                    Toast.makeText(this,
                        it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, StartActivity::class.java))
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show()
    }
}