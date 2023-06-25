package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.R
import com.cmpt362.regathering.databinding.ActivityCreateEventBinding
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import com.cmpt362.regathering.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ViewEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding

    private lateinit var imageView: ImageView

    private val parseDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy")
    private val timeFormat = SimpleDateFormat("h:mm aa")

    private lateinit var eventName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hides buttons and disables edittexts
        setupUiElements()

        val id = intent.getStringExtra("id")
        val fireStore = Firebase.firestore
        imageView = findViewById(R.id.event_image)
        fireStore.collection("events").document(id!!).get().addOnSuccessListener {
            if ((it.get("image") as String).isNotEmpty()){
                imageView.setImageBitmap(stringToBitMap(it.get("image") as String))
            }

            eventName = it.get("name") as String
            binding.eventName.setText(it.get("name") as String)
            binding.eventDescription.setText(it.get("description") as String)

            val parsedDate = parseDateFormat.parse(it.get("date") as String)
            binding.eventDate.setText(dateFormat.format(parsedDate))
            binding.eventTime.setText(timeFormat.format(parsedDate))

            fireStore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get()
                .addOnSuccessListener { userDocument ->
                    val joinedEvents = userDocument.get("joinedEvents") as Collection<String>
                    val isAttendingThisEvent = joinedEvents.contains(it.id)
                    if (!isAttendingThisEvent) {
                        binding.buttonSave.visibility = View.VISIBLE
                    }
                }
        }


        binding.buttonSave.text = "Join"
        binding.buttonSave.setOnClickListener(){
            val user = FirebaseAuth.getInstance().currentUser
            fireStore.collection("users").document(user?.uid!!).update("joinedEvents", FieldValue.arrayUnion(id))
            Toast.makeText(this, "Joined ${eventName}", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.buttonCancel.setOnClickListener(){
            Toast.makeText(this, "Entry discarded.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    private fun setupUiElements() {
        // Hide join button by default
        binding.buttonSave.visibility = View.GONE

        // Hide change button
        binding.changeButton.visibility = View.GONE

        // Disable edittexts
        binding.eventName.isEnabled = false
        binding.eventDescription.isEnabled = false

        // Hide date/time buttons
        binding.dateButton.visibility = View.GONE
        binding.timeButton.visibility = View.GONE
    }
}