package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import com.cmpt362.regathering.model.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class ViewEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val OPTIONS = arrayOf("2022-08-24 12:00", "Computer Science Git Workshop",
        "Come join us for our weekly meeting!", "CSIL Lab Room 2074")

    private lateinit var list_view: ListView
    private lateinit var buttonSave : Button
    private lateinit var buttonCancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        findViewById<Button>(R.id.change_button).visibility = View.GONE

        list_view = findViewById(R.id.list_view_manual_entry)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, OPTIONS)
        list_view.adapter = arrayAdapter

        buttonSave = findViewById(R.id.button_save_manual_entry)
        buttonCancel = findViewById(R.id.button_cancel_manual_entry)

        buttonSave.text = "Join"

        buttonSave.setOnClickListener(){
            val db = Firebase.firestore
            val newEvent = Event()
            newEvent.name = OPTIONS[1]
            newEvent.date = OPTIONS[0]
            newEvent.description = OPTIONS[2]
            newEvent.location = OPTIONS[3]
            db.collection("events").document(UUID.randomUUID().toString()).set(newEvent).addOnSuccessListener {
              it -> println("New event created with the name: ${newEvent.name}")
            }

            finish()
        }
        buttonCancel.setOnClickListener(){
            Toast.makeText(this, "Entry discarded.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun onClickChange(view:View){
        val profilePictureDialogFragment = ProfilePictureDialogFragment()
        profilePictureDialogFragment.show(supportFragmentManager, "tag")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        return
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        return
    }
}