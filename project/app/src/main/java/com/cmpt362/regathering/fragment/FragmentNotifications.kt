package com.cmpt362.regathering.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.cmpt362.regathering.R
import com.cmpt362.regathering.activity.ViewEventActivity
import com.cmpt362.regathering.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentNotifications.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentNotifications : Fragment() {
    private val MESSAGE_VARIETY = arrayOf("\nYou got matched with this new event! (click)\n",
                                                "\nNew event near you! (click)\n",
                                                "\nWe think this event might be right for you! (click)\n")
    private lateinit var listViewNotifications: ListView
    private lateinit var listNotifications: ArrayList<String>

    private lateinit var firestore: FirebaseFirestore
    private lateinit var suggestedEvents: ArrayList<String>

    private lateinit var arrayAdapter: ArrayAdapter<*>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        // initialize firestore
        firestore = FirebaseFirestore.getInstance()

        // suggested events for the user based on interest
        suggestedEvents = ArrayList()

        listViewNotifications = view.findViewById(R.id.list_view_notifications)
        listNotifications = ArrayList()
        arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listNotifications)


        listViewNotifications.adapter = arrayAdapter

        firestore.collection("users").document(FirebaseAuth.getInstance().uid.toString())
            .get().addOnSuccessListener { usersIt ->

            var interests = usersIt.get("interests") as ArrayList<String>
            val hostedEventsByUser = usersIt.get("hostedEvents") as ArrayList<String>

            firestore.collection("events").whereEqualTo("name", interests[0]).get().addOnSuccessListener { eventsIt ->
                for(event in eventsIt){
                    if(!hostedEventsByUser.contains(event.id)){
                        listNotifications.add("New event match: " + event.get("name").toString())
                        arrayAdapter.notifyDataSetChanged()


                        val suggestedEvents: ArrayList<String> = usersIt.get("suggestedEvents") as ArrayList<String>
                        suggestedEvents.add(event.id)

                        // update suggest events
                        firestore.collection("users")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .update("suggestedEvents", suggestedEvents)
                    }
                }

                arrayAdapter.notifyDataSetChanged()
            }
        }
        arrayAdapter.notifyDataSetChanged()


//        listViewNotifications.setOnItemClickListener() { parent: AdapterView<*>, textView: View, position: Int, id: Long ->
//            val activityIntent = Intent(requireActivity(), ViewEventActivity::class.java)
//            activityIntent.putExtra("id", arrayAdapter.getItem(position).toString())
//            startActivity(activityIntent)
//        }

        return view
    }

    override fun onStart() {
        super.onStart()

        if(::arrayAdapter.isInitialized){
            arrayAdapter.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        super.onStop()

        if(::arrayAdapter.isInitialized){
            arrayAdapter.notifyDataSetChanged()
        }
    }
}