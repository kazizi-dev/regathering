package com.cmpt362.regathering.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cmpt362.regathering.R
import com.google.firebase.firestore.DocumentSnapshot

class EventsListAdapter(private val context: Context, private var events: List<DocumentSnapshot>):
    BaseAdapter() {
    override fun getCount(): Int {
        return events.size
    }

    override fun getItem(position: Int): DocumentSnapshot {
        return events[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.item_event, null)
        view.findViewById<TextView>(R.id.eventItemName).text = events[position].get("name").toString()
        view.findViewById<TextView>(R.id.eventItemAddress).text = events[position].get("location").toString()
        view.findViewById<TextView>(R.id.eventItemDate).text = events[position].get("date").toString()
        if((events[position].get("image") as String).isNotEmpty()){
            view.findViewById<ImageView>(R.id.image_event_show).setImageBitmap(stringToBitMap((events[position].get("image") as String)))
        }
        return view
    }

    fun replace(newCommentList: List<DocumentSnapshot>){
        events = newCommentList
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