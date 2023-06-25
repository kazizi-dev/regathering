package com.cmpt362.regathering.fragment

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.cmpt362.regathering.activity.ProfileActivity
import com.cmpt362.regathering.R

/**
 * ProfilePictureDialogFragment used to show a dialog that gives user choice where
 * to get profile image from in the ProfileActivity
 */
class ProfilePictureDialogFragment:DialogFragment(), DialogInterface.OnClickListener {

    private val OPTIONS = arrayOf(
        "Open Camera", "Select from Gallery"
    )
    private lateinit var list_view : ListView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog
        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_profile_picture, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        builder.setTitle("Pick Profile Picture")

        list_view = view.findViewById(R.id.list_view_profile_picture)

        val arrayAdapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, OPTIONS)
        list_view.adapter = arrayAdapter
        list_view.setOnItemClickListener() { parent: AdapterView<*>, textView: View, position: Int, id: Long ->
            if(position == 0){
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(takePictureIntent)
            }
            else{
                val getImageFromGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncherGallery.launch(getImageFromGallery)
            }
        }

        dialog = builder.create()
        return dialog
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            ProfileActivity.myViewModel.userImage.value = result.data?.extras?.get("data") as Bitmap?
            dialog?.dismiss()
        }
    }

    private var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val imageView = requireActivity().findViewById<ImageView>(R.id.profile_image_view)
            imageView.setImageURI(result.data?.data)
            ProfileActivity.myViewModel.userImage.value = (imageView.drawable as BitmapDrawable).bitmap
            dialog?.dismiss()
        }
    }
    override fun onClick(p0: DialogInterface?, p1: Int) {
        return
    }

}