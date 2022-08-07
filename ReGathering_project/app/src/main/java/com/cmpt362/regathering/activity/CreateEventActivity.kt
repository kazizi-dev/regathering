package com.cmpt362.regathering.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import com.cmpt362.regathering.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class CreateEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val OPTIONS = arrayOf("Date", "Name", "Description", "Location")
    private val calendar = Calendar.getInstance()
    private lateinit var list_view: ListView
    private lateinit var buttonSave : Button
    private lateinit var buttonCancel : Button
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var textViewDate: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewDesc: TextView
    private lateinit var textViewLoc: TextView

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    private var displayDate: String = ""
    private var displayName: String = ""
    private var displayDescription: String = ""
    private var displayLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        textViewDate = findViewById(R.id.textViewDateId)
        textViewName = findViewById(R.id.textViewNameId)
        textViewDesc = findViewById(R.id.textViewDescriptionId)
        textViewLoc = findViewById(R.id.textViewLocationId)

        list_view = findViewById(R.id.list_view_manual_entry)
        buttonSave = findViewById(R.id.button_save_manual_entry)
        buttonCancel = findViewById(R.id.button_cancel_manual_entry)

        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, OPTIONS)
        list_view.adapter = arrayAdapter

        list_view.setOnItemClickListener() { parent: AdapterView<*>, v: View, position: Int, id: Long ->

            arrayAdapter.notifyDataSetChanged()
            list_view.adapter = arrayAdapter

            Toast.makeText(this, "i = $position.", Toast.LENGTH_SHORT).show()
            if(position == 0){
                val datePickerDialog = DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()

                getDateTimeCalendar()
            }
            else if(position == 1) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setTitle(OPTIONS[position])

                builder.setPositiveButton("ok") { dialog, which ->
                    val title: String = OPTIONS[position]
                    textViewName.text = "$title: ${input.text}"
                    displayName = input.text.toString()
                }
                builder.setNegativeButton("cancel"){dialog, which -> }
                builder.show()
            }
            else if(position == 2) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setTitle(OPTIONS[position])

                builder.setPositiveButton("ok") { dialog, which ->
                    val title: String = OPTIONS[position]
                    textViewDesc.text = "$title: ${input.text}"
                    displayDescription = input.text.toString()
                }
                builder.setNegativeButton("cancel"){dialog, which -> }
                builder.show()
            }
            else if(position == 3) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setTitle(OPTIONS[position])

                builder.setPositiveButton("ok") { dialog, which ->
                    val title: String = OPTIONS[position]
                    textViewLoc.text = "$title: ${input.text}"
                    displayLocation = input.text.toString()
                }
                builder.setNegativeButton("cancel"){dialog, which -> }
                builder.show()
            }
        }

        buttonSave.setOnClickListener(){
            val db = Firebase.firestore
            val newEvent = Event()
            newEvent.name = displayName
            newEvent.date = displayDate
            newEvent.description = displayDescription
            newEvent.location = displayLocation
            val id = UUID.randomUUID().toString()
            db.collection("events").document(id).set(newEvent).addOnSuccessListener {
              it -> println("new event created with name: ${newEvent.name}")
                db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .update("hostedEvents", FieldValue.arrayUnion(id))
            }


            finish()
        }
        buttonCancel.setOnClickListener(){
            Toast.makeText(this, "Entry discarded.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    override fun onResume() {
        super.onResume()

        // update adapter
        arrayAdapter.notifyDataSetChanged()
        list_view.adapter = arrayAdapter
    }


    private fun getDateTimeCalendar(){
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
    }

    fun onClickChange(view:View){
        val profilePictureDialogFragment = ProfilePictureDialogFragment()
        profilePictureDialogFragment.show(supportFragmentManager, "tag")
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        savedMinute = minute

        textViewDate.text = "Date: $savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00"
        displayDate = "$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00"
    }
}