package com.vau.myappmanager.ui.adapter

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter (fm : FragmentManager): FragmentStatePagerAdapter(fm){
    val fragmentList:MutableList<Fragment> = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    fun addFragment(fragment:Fragment){
        fragmentList.add(fragment)
    }
}