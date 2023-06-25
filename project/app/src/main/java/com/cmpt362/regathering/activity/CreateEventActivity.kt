package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cmpt362.regathering.R
import com.cmpt362.regathering.databinding.ActivityCreateEventBinding
import com.cmpt362.regathering.fragment.EventPictureDialogFragment
import com.cmpt362.regathering.model.Event
import com.cmpt362.regathering.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * This class handles the user interface and logic related to
 * creating a new event by the user.
 */

class CreateEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()
    private lateinit var imageView: ImageView

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    private val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy")
    private val timeFormat = SimpleDateFormat("h:mm aa")

    private lateinit var binding: ActivityCreateEventBinding

    companion object {
        lateinit var myViewModel: MyViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflate the layout based on the xml file
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        initEventImage()
        initDate()
        initButtons()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year

        // Update date view
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        binding.eventDate.text = dateFormat.format(calendar.time)
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        savedMinute = minute

        // Update time view
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minute)
        binding.eventTime.text = timeFormat.format(calendar.time)
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun initEventImage(){
        imageView = findViewById(R.id.event_image)

        // store event picture
        myViewModel.eventImage.observe(this) {
            imageView.setImageBitmap(it)
        }
    }

    private fun initDate() {
        // Add one week to the initial data
        calendar.add(Calendar.WEEK_OF_YEAR, 1)

        savedYear = calendar.get(Calendar.YEAR)
        savedMonth = calendar.get(Calendar.MONTH)
        savedDay = calendar.get(Calendar.DAY_OF_MONTH)
        savedHour = calendar.get(Calendar.HOUR)
        savedMinute = calendar.get(Calendar.MINUTE)

        // Update view
        binding.eventDate.text = dateFormat.format(calendar.time)
        binding.eventTime.text = timeFormat.format(calendar.time)
    }

    private fun initButtons() {
        // change event image
        binding.changeButton.setOnClickListener {
            val eventPictureDialogFragment = EventPictureDialogFragment()
            eventPictureDialogFragment.show(supportFragmentManager, "tag")
        }

        // date button
        binding.dateButton.setOnClickListener {
            DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // time button
        binding.timeButton.setOnClickListener {
            TimePickerDialog(this, this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show()
        }

        // save button
        binding.buttonSave.setOnClickListener {
            val db = Firebase.firestore
            val newEvent = Event()
            newEvent.name = binding.eventName.text.toString()
            newEvent.date = "$savedYear-${savedMonth + 1}-$savedDay $savedHour:$savedMinute:00"
            newEvent.description = binding.eventDescription.text.toString()
            newEvent.location = binding.eventLocation.text.toString()
            newEvent.image = bitMapToString((imageView.drawable as BitmapDrawable).bitmap)

            val id = UUID.randomUUID().toString()
            db.collection("events").document(id).set(newEvent).addOnSuccessListener {
                println("new event created with name: ${newEvent.name}")
                db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .update("hostedEvents", FieldValue.arrayUnion(id))
            }

            finish()
        }

        // cancel button
        binding.buttonCancel.setOnClickListener {
            Toast.makeText(this, "Entry discarded.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}