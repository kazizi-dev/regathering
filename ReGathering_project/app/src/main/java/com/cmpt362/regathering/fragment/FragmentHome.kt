package com.cmpt362.regathering.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmpt362.regathering.R
import com.cmpt362.regathering.adapter.EventAdapter
//import com.cmpt362.regathering.adapter.EventListAdapter
import com.cmpt362.regathering.database.Event
import com.cmpt362.regathering.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * FragmentHome class that extends Fragment class
 * used to display tab that shows a list of events
 */
class FragmentHome: Fragment(),
    EventAdapter.OnEventSelectedListener {
    companion object {
        private const val LIMIT = 50
    }

    lateinit var firestore: FirebaseFirestore
    lateinit var eventsQuery: Query

    private lateinit var binding: FragmentHomeBinding
    lateinit var eventAdapter: EventAdapter

    private lateinit var fragmentView: View
//    private lateinit var eventArrayAdapter: EventListAdapter
    private lateinit var listViewEvents: ListView
    private lateinit var eventArrayList: ArrayList<Event>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get instance of firestore
        firestore = Firebase.firestore

        eventsQuery = firestore.collection("events")
            .orderBy("date", Query.Direction.ASCENDING)
            .limit(LIMIT.toLong())

        // RecyclerView
        eventAdapter = object : EventAdapter(eventsQuery, this@FragmentHome) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerEvents.visibility = View.GONE
                } else {
                    binding.recyclerEvents.visibility = View.VISIBLE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerEvents.layoutManager = layoutManager
        binding.recyclerEvents.adapter = eventAdapter
    }

    override fun onStart() {
        super.onStart()

        // Start listening for Firestore updates
        eventAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        eventAdapter.stopListening()
    }

    override fun onEventSelected(event: DocumentSnapshot) {
        // TODO: Go to the details page for the selected restaurant
        println("DEBUG: Selected event -> $event")
    }
}