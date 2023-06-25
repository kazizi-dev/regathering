package com.cmpt362.regathering.activity


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cmpt362.regathering.R
import com.cmpt362.regathering.Util
import com.cmpt362.regathering.fragment.ProfilePictureDialogFragment
import com.cmpt362.regathering.viewmodel.MyViewModel
import java.io.ByteArrayOutputStream

/**
 * ProfileActivity used to store users profile
 * where user can set his profile image and profile preferences
 */
class ProfileActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    companion object{
        lateinit var myViewModel: MyViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Util.checkPermissions(this)
        imageView = findViewById(R.id.profile_image_view)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.userImage.observe(this) { it ->
            imageView.setImageBitmap(it)
        }

        loadProfile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    fun onClickChange(view:View){
        val profilePictureDialogFragment = ProfilePictureDialogFragment()
        profilePictureDialogFragment.show(supportFragmentManager, "tag")
    }

    fun onClickSave(view:View){
        saveProfile()
        onClickCancel(view)
    }

    fun onClickCancel(view: View) {
        finish()
    }

    private fun loadProfile(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val userName = sharedPref.getString(getString(R.string.saved_username), "")
        val email = sharedPref.getString(getString(R.string.saved_email), "")
        val phoneNumber = sharedPref.getString(getString(R.string.saved_number), "")
        val gender = sharedPref.getInt(getString(R.string.saved_gender), 2)
        val classYear = sharedPref.getString(getString(R.string.saved_class_year), "")
        val major = sharedPref.getString(getString(R.string.saved_major), "")
        val imageString = sharedPref.getString(getString(R.string.saved_image), "")
        findViewById<EditText>(R.id.name_editview).setText(userName)
        findViewById<EditText>(R.id.email_editview).setText(email)
        findViewById<EditText>(R.id.phone_editview).setText(phoneNumber)
        findViewById<EditText>(R.id.class_editview).setText(classYear)
        findViewById<EditText>(R.id.major_editview).setText(major)
        if(gender == 0){
            findViewById<RadioButton>(R.id.female_radiobutton).isChecked = true
        }
        else if(gender == 1){
            findViewById<RadioButton>(R.id.male_radiobutton).isChecked = true
        }
        if(imageString != ""){
            findViewById<ImageView>(R.id.profile_image_view).setImageBitmap(stringToBitMap(imageString))
        }
        else{
            findViewById<ImageView>(R.id.profile_image_view).setImageResource(R.drawable.dart)
        }

    }

    private fun saveProfile(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.saved_username), findViewById<EditText>(R.id.name_editview).text.toString())
            putString(getString(R.string.saved_email), findViewById<EditText>(R.id.email_editview).text.toString())
            putString(getString(R.string.saved_number), findViewById<EditText>(R.id.phone_editview).text.toString())
            putString(getString(R.string.saved_class_year), findViewById<EditText>(R.id.class_editview).text.toString())
            putString(getString(R.string.saved_major), findViewById<EditText>(R.id.major_editview).text.toString())
            val bm = (findViewById<ImageView>(R.id.profile_image_view).drawable as BitmapDrawable).bitmap
            putString(getString(R.string.saved_image), bitMapToString(bm))
            if(findViewById<RadioButton>(R.id.female_radiobutton).isChecked){
                putInt(getString(R.string.saved_gender), 0)
            }
            else if(findViewById<RadioButton>(R.id.male_radiobutton).isChecked){
                putInt(getString(R.string.saved_gender), 1)
            }
            else{
                putInt(getString(R.string.saved_gender), 2)
            }
            Toast.makeText(applicationContext, "Successfully saved profile information", Toast.LENGTH_SHORT).show()
            apply()
        }
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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