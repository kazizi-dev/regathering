package com.cmpt362.regathering.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.cmpt362.regathering.R
import com.cmpt362.regathering.activity.CreateEventActivity
import com.cmpt362.regathering.activity.ViewEventActivity
import com.cmpt362.regathering.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * FragmentEventsSearch class that shows up when user chooses History from the navigation bar
 * in the StartActivity
 */
class FragmentEventsSearch: Fragment() {
    private lateinit var listViewResults: ListView
    private lateinit var listResults: ArrayList<String>
    private lateinit var btnSearch: Button
    private lateinit var btnCreateEvent: Button
    private lateinit var editTextSearch: EditText
    lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = Firebase.firestore
        val view = inflater.inflate(R.layout.fragment_events_search, container, false)
        listViewResults = view.findViewById(R.id.list_view_search)
        btnSearch = view.findViewById(R.id.search_button)
        editTextSearch = view.findViewById(R.id.edit_text_search)
        btnCreateEvent = view.findViewById(R.id.createEvent_button)
        listResults = ArrayList()
        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listResults)
        listViewResults.adapter = arrayAdapter

        listViewResults.setOnItemClickListener() { parent: AdapterView<*>, textView: View, position: Int, id: Long ->
            val activityIntent = Intent(requireActivity(), ViewEventActivity::class.java)
            startActivity(activityIntent)

        }

        val myViewModel = MyViewModel()

        btnSearch.setOnClickListener {
            listResults.clear()

            val input = editTextSearch.text.toString().lowercase().trim()
            val eventsRef = firestore.collection("events")
            val events = eventsRef.whereIn("name", listOf(input)).get().addOnSuccessListener {
                it.documents
            }
            println("debug: $events")
            if(input == "computer science"){
                listResults.addAll(myViewModel.SEARCHED_EVENTS_COMPUTER_SCIENCE)
            }
            else if(input == "meetup"){
                listResults.addAll(myViewModel.SEARCHED_EVENTS_MEETUP)
            }
            arrayAdapter.notifyDataSetChanged()
        }

        btnCreateEvent.setOnClickListener(){
            val activityIntent = Intent(requireActivity(), CreateEventActivity::class.java)
            startActivity(activityIntent)
        }

        return view
    }
}