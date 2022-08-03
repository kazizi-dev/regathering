package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet.GONE
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.InputDialogFragment
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import java.util.*


class ViewEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val OPTIONS = arrayOf("Date: July 20th, 2022", "12:00pm", "Computer Science meeting",
        "Come join us for our weekly meeting!", "SFU student union building")

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

        buttonSave.setText("Join")

        buttonSave.setOnClickListener(){
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