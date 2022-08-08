package com.cmpt362.regathering.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import com.cmpt362.regathering.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class ViewEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val OPTIONS = arrayOf("2022-08-24 12:00", "Computer Science Git Workshop",
        "Come join us for our weekly meeting!", "CSIL Lab Room 2074")

    private lateinit var list_view: ListView
    private lateinit var buttonJoin : Button
    private lateinit var buttonCancel : Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        findViewById<Button>(R.id.change_button).visibility = View.GONE
        val arrayList = ArrayList<String>()
        val id = intent.getStringExtra("id")
        val fireStore = Firebase.firestore
        list_view = findViewById(R.id.list_view_manual_entry)
        imageView = findViewById(R.id.event_image)
        fireStore.collection("events").document(id!!).get().addOnSuccessListener {
            if((it.get("image") as String).isNotEmpty()){
                imageView.setImageBitmap(stringToBitMap(it.get("image") as String))
            }
            arrayList.add(it.get("date") as String)
            arrayList.add(it.get("name") as String)
            arrayList.add(it.get("description") as String)
            arrayList.add(it.get("location") as String)
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)
            list_view.adapter = arrayAdapter
        }
        buttonJoin = findViewById(R.id.button_save_manual_entry)
        buttonCancel = findViewById(R.id.button_cancel_manual_entry)

        buttonJoin.text = "Join"

        buttonJoin.setOnClickListener(){
            val user = FirebaseAuth.getInstance().currentUser
            println("debug: user ${user?.uid}")
            fireStore.collection("users").document(user?.uid!!).update("joinedEvents", FieldValue.arrayUnion(id))
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
}