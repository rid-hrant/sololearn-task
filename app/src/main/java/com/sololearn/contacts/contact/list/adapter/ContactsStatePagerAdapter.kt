package com.sololearn.contacts.contact.list.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sololearn.contacts.contact.list.pojo.Screen

class ContactsStatePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val screens = ArrayList<Screen>()

    override fun getItem(position: Int): Fragment {
        return screens[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return screens[position].title
    }

    override fun getCount(): Int {
        return screens.size
    }

    fun addScreen(screen: Screen) {
        screens.add(screen)
    }
}