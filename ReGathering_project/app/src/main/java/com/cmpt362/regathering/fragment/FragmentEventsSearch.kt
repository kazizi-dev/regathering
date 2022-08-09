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
import com.cmpt362.regathering.activity.EventsListAdapter
import com.cmpt362.regathering.activity.ViewEventActivity
import com.cmpt362.regathering.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


/**
 * FragmentEventsSearch class that shows up when user chooses History from the navigation bar
 * in the StartActivity
 */
class FragmentEventsSearch: Fragment() {
    private lateinit var listViewResults: ListView
    private lateinit var listResults: ArrayList<DocumentSnapshot>
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
        val arrayAdapter = EventsListAdapter(requireActivity(), listResults)
        listViewResults.adapter = arrayAdapter

        listViewResults.setOnItemClickListener() { parent: AdapterView<*>, textView: View, position: Int, id: Long ->
            println("debug: inside listener")
            val activityIntent = Intent(requireActivity(), ViewEventActivity::class.java)
            println("debug: event id ${arrayAdapter.getItem(position).id}")
            activityIntent.putExtra("id", arrayAdapter.getItem(position).id)
            startActivity(activityIntent)
        }

        val myViewModel = MyViewModel()

        btnSearch.setOnClickListener {
            listResults.clear()
            val input = editTextSearch.text.toString().trim()
            val inputParsed = input.split("$[^w']+")
            val inputParsedLower = ArrayList<String>()
            for(word in inputParsed){
                inputParsedLower.add(word.lowercase())
            }
            val eventsRef = firestore.collection("events")
            listResults.clear()
            eventsRef.get().addOnSuccessListener {
                for(document in it.documents){
                    val arrayName = document.get("name") as String
                    if(arrayName == input){
                        listResults.add(document)
                    }
                    else{
                        val arrayNameSplit = arrayName.split(" ")
                        val lowerCaseArrayName = ArrayList<String>()
                        for(name in arrayNameSplit) {
                            lowerCaseArrayName.add(name.lowercase())
                        }
                        for(name in lowerCaseArrayName){
                            for(name_input in inputParsedLower){
                                if(name == name_input){
                                    listResults.add(document)
                                }
                            }
                        }
                    }
                }
                arrayAdapter.replace(listResults)
                arrayAdapter.notifyDataSetChanged()
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