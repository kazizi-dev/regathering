package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.InputDialogFragment
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import java.util.*


class CreateEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val OPTIONS = arrayOf("Date", "Time", "Name", "Comment", "Location")
    private val calendar = Calendar.getInstance()
    private lateinit var list_view: ListView
    private lateinit var buttonSave : Button
    private lateinit var buttonCancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        list_view = findViewById(R.id.list_view_manual_entry)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, OPTIONS)
        list_view.adapter = arrayAdapter
        list_view.setOnItemClickListener() { parent: AdapterView<*>, textView: View, position: Int, id: Long ->
            if(position == 0){
                val datePickerDialog = DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()
            }
            else if (position == 1){
                val timePickerDialog = TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePickerDialog.show()
            }
            else{
                val inputDialogFragment = InputDialogFragment()
                val bundle = Bundle()
                bundle.putInt(InputDialogFragment.DIALOG_KEY, position)
                inputDialogFragment.arguments = bundle
                inputDialogFragment.show(supportFragmentManager, "tag")
            }
            println("debug: ${parent.toString()}")
        }
        buttonSave = findViewById(R.id.button_save_manual_entry)
        buttonCancel = findViewById(R.id.button_cancel_manual_entry)
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