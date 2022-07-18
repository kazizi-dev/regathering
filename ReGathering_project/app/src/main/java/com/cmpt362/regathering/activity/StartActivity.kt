package com.cmpt362.regathering.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cmpt362.regathering.R
import com.cmpt362.regathering.fragment.FragmentEventsSearch
import com.cmpt362.regathering.fragment.FragmentHome
import com.cmpt362.regathering.fragment.FragmentNotifications
import com.cmpt362.regathering.fragment.FragmentSettings
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList

/**
 * StartActivity which is the launcher activity.
 * Used to show and store the fragments in the navigation tab
 */
class StartActivity : AppCompatActivity() {
    private lateinit var fragmentHome: FragmentHome
    private lateinit var fragmentEventsSearch: FragmentEventsSearch
    private lateinit var fragmentSettings: FragmentSettings
    private lateinit var fragmentNotifications: FragmentNotifications
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tab: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var myFragmentStateAdapter: StartActivityFragmentStateAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private val TAB_TEXT = arrayOf("home", "search", "notifications", "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        fragmentHome = FragmentHome()
        fragmentEventsSearch = FragmentEventsSearch()
        fragmentSettings = FragmentSettings()
        fragmentNotifications = FragmentNotifications()

        fragments = ArrayList()
        fragments.add(fragmentHome)
        fragments.add(fragmentEventsSearch)
        fragments.add(fragmentNotifications)
        fragments.add(fragmentSettings)

        tab = findViewById(R.id.tab)
        viewPager = findViewById(R.id.viewpager)
        myFragmentStateAdapter = StartActivityFragmentStateAdapter(this, fragments)
        viewPager.adapter = myFragmentStateAdapter

        tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy(){
            tab: TabLayout.Tab, position: Int ->
            tab.text = TAB_TEXT[position]
        }
        tabLayoutMediator = TabLayoutMediator(tab, viewPager, tabConfigurationStrategy)
        tabLayoutMediator.attach()
    }
}