package com.cmpt362.regathering.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * StartActivityFragmentStateAdapter used as a helper class
 * for the display of fragments in the StartActivity
 */
class StartActivityFragmentStateAdapter(activity: FragmentActivity, var list:ArrayList<Fragment>):
    FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}