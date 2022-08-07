package com.cmpt362.regathering.fragment

//import com.cmpt362.regathering.adapter.EventListAdapter
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmpt362.regathering.R
import com.cmpt362.regathering.adapter.EventAdapter
import com.cmpt362.regathering.databinding.FragmentHomeBinding
import com.cmpt362.regathering.model.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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

    var VIEW_OPTIONS = arrayOf("Hosted Events, Joined Events")

    lateinit var firestore: FirebaseFirestore
    lateinit var eventsQuery: Query

    private lateinit var binding: FragmentHomeBinding
    lateinit var eventAdapter: EventAdapter
    private lateinit var fragmentView: View
    private lateinit var listViewEvents: ListView
    private lateinit var eventArrayList: ArrayList<Event>
    private var hostedEvents: Boolean = true

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
        getEventsFromDatabase("hostedEvents")
        binding.switchViewEvents.setOnClickListener(){
            hostedEvents = !hostedEvents
            if(hostedEvents){
                binding.switchViewEvents.text = "Hosted Events"
                binding.upcomingEvents.text = "Hosted Events"
                getEventsFromDatabase("hostedEvents")
            }
            else{
                binding.switchViewEvents.text = "Joined Events"
                binding.upcomingEvents.text = "Joined Events"
                getEventsFromDatabase("joinedEvents")
            }

        }

    }

    private fun getEventsFromDatabase(eventsType: String){
        firestore = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser!!.uid
        firestore.collection("users").document(user).get().addOnSuccessListener {
            println("debug: user $it")
            val hostedEvents = ArrayList<String>()
            hostedEvents.addAll(it.get(eventsType) as Collection<String>)
            println("debug: hostedEvents $hostedEvents")
            eventsQuery = firestore.collection("events")
                .whereIn(FieldPath.documentId(), hostedEvents)
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
            binding.recyclerEvents.addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerEvents.context,
                    LinearLayoutManager.HORIZONTAL
                ))
            eventAdapter.startListening()
        }
    }

    override fun onStart() {
        super.onStart()
        if(::eventAdapter.isInitialized){
            eventAdapter.startListening()
        }
        // Start listening for Firestore updates
        //eventAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        eventAdapter.stopListening()
    }

    override fun onEventSelected(event: DocumentSnapshot) {
        // TODO: Go to the details page for the selected restaurant
        println("DEBUG: Selected event -> $event")
        if(hostedEvents){
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cancel Event?")

            builder.setPositiveButton("Yes") { dialog, which ->
                firestore.collection("events").document(event.id)
                    .delete()
                    .addOnSuccessListener{ Log.d(TAG,"DocumentSnapshot successfully deleted!")}
                    .addOnSuccessListener{e->Log.w(TAG, "Error deleting document: $e")}
            }
            builder.setNegativeButton("No"){dialog, which -> }
            builder.show()
        }
        else{
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cancel Attendance?")

            builder.setPositiveButton("Yes") { dialog, which ->
                firestore.collection("events").document(event.id)
                    .delete()
                    .addOnSuccessListener{ Log.d(TAG,"DocumentSnapshot successfully deleted!")}
                    .addOnSuccessListener{e->Log.w(TAG, "Error deleting document: $e")}
            }
            builder.setNegativeButton("No"){dialog, which -> }
            builder.show()
        }
    }
}