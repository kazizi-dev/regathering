package com.cmpt362.regathering.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.cmpt362.regathering.R

/**
 * GpsActivity opens when user chooses GPS in Input type of the Start fragment
 * TODO: still in development
 */
class GpsActivity : AppCompatActivity() {
    private lateinit var buttonSave : Button
    private lateinit var buttonCancel : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        buttonSave = findViewById(R.id.button_save_gps)
        buttonCancel = findViewById(R.id.button_cancel_gps)

        buttonSave.setOnClickListener(){
            finish()
        }
        buttonCancel.setOnClickListener(){
            finish()
        }
    }
}