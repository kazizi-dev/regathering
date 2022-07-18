package com.cmpt362.regathering.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.cmpt362.regathering.R

/**
 * InputDialogFragment used to show bunch of dialogs that are needed when
 * user chooses Manual Entry in the Input type in the Start fragment
 */
class InputDialogFragment:DialogFragment(), DialogInterface.OnClickListener {

    companion object{
        const val DIALOG_KEY = "key"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog
        val bundle = arguments
        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_input_dialog, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        if(bundle?.getInt(DIALOG_KEY) == 2){
            builder.setTitle("Duration")
        }
        else if(bundle?.getInt(DIALOG_KEY) == 3){
            builder.setTitle("Distance")
        }
        else if(bundle?.getInt(DIALOG_KEY) == 4){
            builder.setTitle("Calories")
        }
        else if(bundle?.getInt(DIALOG_KEY) == 5){
            builder.setTitle("Heart Rate")
        }
        else if(bundle?.getInt(DIALOG_KEY) == 6){
            builder.setTitle("Comment")
            val editText = view.findViewById<EditText>(R.id.edit_text_input_fragment)
            editText.inputType = EditText.AUTOFILL_TYPE_TEXT
            editText.hint = "How did it go? Notes here."
        }
        builder.setPositiveButton("ok", this)
        builder.setNegativeButton("cancel", this)
        dialog = builder.create()
        return dialog
    }
    override fun onClick(dialog: DialogInterface, item: Int) {
        if(item == DialogInterface.BUTTON_POSITIVE){
            return
        }
        if(item == DialogInterface.BUTTON_NEGATIVE){
            return
        }
    }

}