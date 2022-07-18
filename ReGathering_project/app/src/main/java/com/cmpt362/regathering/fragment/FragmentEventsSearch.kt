package com.cmpt362.regathering.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cmpt362.regathering.R


/**
 * FragmentEventsSearch class that shows up when user chooses History from the navigation bar
 * in the StartActivity
 */
class FragmentEventsSearch: Fragment() {
    private var SEARCHED_EVENTS_COMPUTER_SCIENCE = arrayOf(
                        "Computer Science meetup\n888 University Drive" +
                            "\nJuly 30th, 2022",
                        "Computer Science CO-OP workshop\n888 University Drive" +
                                "\nJuly 20th, 2022",
                        "Presentation: Moral issues in Computer Science\n888 University Drive" +
                                "\nAugust 3rd, 2022")
    private var SEARCHED_EVENTS_MEETUP = arrayOf(
                        "Computer Science meetup\n888 University Drive" +
                            "\nJuly 30th, 2022",
                        "Biology meetup\n888 University Drive" +
                                "\nJuly 25th, 2022",
                        "Chemistry meetup\n888 University Drive" +
                                "\nAugust 1st, 2022")
    private lateinit var listViewResults: ListView
    private lateinit var listResults: ArrayList<String>
    private lateinit var btnSearch: Button
    private lateinit var editTextSearch: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events_search, container, false)
        listViewResults = view.findViewById(R.id.list_view_search)
        btnSearch = view.findViewById(R.id.search_button)
        editTextSearch = view.findViewById(R.id.edit_text_search)
        listResults = ArrayList()
        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listResults)
        listViewResults.adapter = arrayAdapter

        btnSearch.setOnClickListener {
            listResults.clear()

            val input = editTextSearch.text.toString().lowercase().trim()
            if(input == "computer science"){
                listResults.addAll(SEARCHED_EVENTS_COMPUTER_SCIENCE)
            }
            else if(input == "meetup"){
                listResults.addAll(SEARCHED_EVENTS_MEETUP)
            }
            arrayAdapter.notifyDataSetChanged()
        }

        return view
    }
}