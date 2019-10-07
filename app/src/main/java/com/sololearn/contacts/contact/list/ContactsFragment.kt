package com.sololearn.contacts.contact.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sololearn.contacts.R
import com.sololearn.contacts.contact.edit.EditContactActivity
import com.sololearn.contacts.contact.list.adapter.ContactsRecyclerViewAdapter
import com.sololearn.contacts.contact.list.adapter.ContactsRecyclerViewAdapter.OnClickListener
import com.sololearn.contacts.contact.list.viewmodel.ContactsViewModel
import com.sololearn.contacts.contact.list.viewmodel.ContactsViewModelFactory
import com.sololearn.contacts.databinding.FragmentContactsBinding
import com.sololearn.contacts.networking.Constants
import com.sololearn.contacts.networking.dto.ContactDto


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactsRecyclerViewAdapter: ContactsRecyclerViewAdapter
    private var contactsViewModel: ContactsViewModel? = null

    companion object {
        const val REQUEST_CODE_EDIT_ACTIVITY = 1

        fun getInstance(orderBy: Constants.OrderBy): ContactsFragment {
            val contactsFragment = ContactsFragment()
            val bundle = Bundle()
            bundle.putString(ContactsViewModel.ORDER_BY, orderBy.type)
            contactsFragment.arguments = bundle
            return contactsFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        contactsViewModel = ViewModelProviders.of(this, arguments?.let {
            ContactsViewModelFactory(
                it
            )
        })
            .get(ContactsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contactsRecyclerViewAdapter =
            ContactsRecyclerViewAdapter(object : OnClickListener {

                override fun onEditClick(contact: ContactDto) {
                    val intent = Intent(activity, EditContactActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable(EditContactActivity.EXTRA_KEY_CONTACT, contact)
                    intent.putExtras(bundle)
                    startActivityForResult(
                        intent,
                        REQUEST_CODE_EDIT_ACTIVITY
                    )
                }

                override fun onDeleteClick(contact: ContactDto) {
                    contactsViewModel?.deleteContact(contact)
                }

            })

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewContacts.apply {
            layoutManager = linearLayoutManager
            adapter = contactsRecyclerViewAdapter
        }

        contactsViewModel?.contacts!!.observe(this, Observer {
            contactsRecyclerViewAdapter.apply {
                contacts.addAll(if (contacts.size > 0) contacts.size - 1 else 0, it)
                notifyDataSetChanged()
            }
        })

        contactsViewModel?.deleteContact!!.observe(this, Observer {
            (activity as ContactsActivity).deleteContact(it)
        })

        contactsViewModel?.error?.observe(this, Observer {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        })

        var loading = true
        var pastVisibleItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        binding.recyclerViewContacts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            contactsViewModel!!.loadContacts(contactsRecyclerViewAdapter.contacts.size)
                        }
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        (activity as ContactsActivity).onActivityResult(requestCode, resultCode, data)
    }

    fun updateContact(contactDto: ContactDto) {
        contactsRecyclerViewAdapter.updateContact(contactDto)
    }

    fun deleteContact(contactDto: ContactDto) {
        contactsRecyclerViewAdapter.deleteContact(contactDto)
    }

    fun refreshContacts() {
        contactsViewModel?.loadContacts(contactsRecyclerViewAdapter.contacts.size)
    }
}