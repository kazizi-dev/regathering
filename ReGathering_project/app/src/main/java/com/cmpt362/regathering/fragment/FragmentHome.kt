package com.cmpt362.regathering.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cmpt362.regathering.R

/**
 * FragmentHome class that extends Fragment class
 * used to display tab that allows user to start tracking sports activities.
 * Fragment triggered when user opens Start tab in the navigation bar
 */
class FragmentHome:Fragment() {
    private var UPCOMING_LIST = arrayOf("Computer Science meetup\n888 University Drive" +
                                        "\nJuly 30th, 2022",
                                        "Biology meetup\n888 University Drive" +
                                                "\nJuly 25th, 2022",
                                        "Chemistry meetup\n888 University Drive" +
                                                "\nAugust 1st, 2022")
    private var SUGGESTED_LIST = arrayOf("Science Conference\n1808 West 3rd avenue" +
            "\nAugust 10th, 2022",
        "Hackathon\n2525 Richards street" +
                "\nSeptember 1st, 2022")
    private lateinit var listViewUpcomingEvents: ListView
    private lateinit var listUpcomingEvents: ArrayList<String>
    private lateinit var listViewSuggestedEvents: ListView
    private lateinit var listSuggestedEvents: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        listViewUpcomingEvents = view.findViewById(R.id.upcoming_events_list_view)
        listViewSuggestedEvents = view.findViewById(R.id.suggested_events_list_view)
        listUpcomingEvents = ArrayList()
        listSuggestedEvents = ArrayList()
        val arrayAdapterUpcoming = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listUpcomingEvents)
        listViewUpcomingEvents.adapter = arrayAdapterUpcoming
        val arrayAdapterSuggested = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listSuggestedEvents)
        listViewSuggestedEvents.adapter = arrayAdapterSuggested
        listUpcomingEvents.addAll(UPCOMING_LIST)
        listSuggestedEvents.addAll(SUGGESTED_LIST)
        return view
    }
}