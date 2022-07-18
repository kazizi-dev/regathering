package com.cmpt362.regathering.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.cmpt362.regathering.R


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentNotifications.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentNotifications : Fragment() {
    private val NOTIFICATION_CONTENT = arrayOf("\nSomeone added you as a friend!\n",
                                                "\nHackathon events starts in 24 hours!\n",
                                                "\nSuggested event: Vancouver Marathon!\n")
    private lateinit var listViewNotifications: ListView
    private lateinit var listNotifications: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        listViewNotifications = view.findViewById(R.id.list_view_notifications)
        listNotifications = ArrayList()
        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listNotifications)
        listViewNotifications.adapter = arrayAdapter
        listNotifications.addAll(NOTIFICATION_CONTENT)
        return view
    }

}