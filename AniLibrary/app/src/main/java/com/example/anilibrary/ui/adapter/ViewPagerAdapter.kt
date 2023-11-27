package com.example.anilibrary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(list: ArrayList<Fragment>,fm: FragmentManager, lifecycle: androidx.lifecycle.Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList: ArrayList<Fragment> = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}