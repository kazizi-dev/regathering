package com.cmpt362.regathering.activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        return view
    }

    fun replace(newCommentList: List<DocumentSnapshot>){
        events = newCommentList
    }
}