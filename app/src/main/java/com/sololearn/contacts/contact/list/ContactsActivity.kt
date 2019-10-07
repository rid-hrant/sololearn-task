package com.sololearn.contacts.contact.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sololearn.contacts.R
import com.sololearn.contacts.contact.add.AddContactActivity
import com.sololearn.contacts.contact.edit.EditContactActivity
import com.sololearn.contacts.contact.list.adapter.ContactsStatePagerAdapter
import com.sololearn.contacts.contact.list.pojo.Screen
import com.sololearn.contacts.databinding.ActivityContactsBinding
import com.sololearn.contacts.networking.Constants
import com.sololearn.contacts.networking.dto.ContactDto

class ContactsActivity : AppCompatActivity() {

    private val screens: MutableList<Screen> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityContactsBinding>(this, R.layout.activity_contacts)

        screens.add(Screen(ContactsFragment.getInstance(Constants.OrderBy.DATE), getString(R.string.date)))
        screens.add(Screen(ContactsFragment.getInstance(Constants.OrderBy.NAME), getString(R.string.name)))
        screens.add(Screen(ContactsFragment.getInstance(Constants.OrderBy.NUMBER), getString(R.string.number)))

        val contactsAdapter = ContactsStatePagerAdapter(
            supportFragmentManager
        ).apply {
            addScreen(screens[0])
            addScreen(screens[1])
            addScreen(screens[2])
        }

        binding.viewPagerContacts.apply {
            adapter = contactsAdapter
            offscreenPageLimit = 3
        }

        binding.tabLayout.setupWithViewPager(binding.viewPagerContacts)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ContactsFragment.REQUEST_CODE_EDIT_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val contactDto = data?.extras?.getParcelable<ContactDto>(EditContactActivity.EXTRA_KEY_CONTACT)

            screens.forEach {
                (it.fragment as ContactsFragment).updateContact(contactDto!!)
            }
        } else if (requestCode == REQUEST_CODE_ADD_CONTACT_ACTIVITY && resultCode == Activity.RESULT_OK) {
            screens.forEach {
                (it.fragment as ContactsFragment).refreshContacts()
            }
        }
    }

    fun deleteContact(contact: ContactDto) {
        screens.forEach {
            (it.fragment as ContactsFragment).deleteContact(contact)
        }
    }

    fun onAddContact(@Suppress("UNUSED_PARAMETER") v: View) {
        startActivityForResult(
            Intent(this, AddContactActivity::class.java),
            REQUEST_CODE_ADD_CONTACT_ACTIVITY
        )
    }

    companion object {
        const val REQUEST_CODE_ADD_CONTACT_ACTIVITY = 2
    }
}
