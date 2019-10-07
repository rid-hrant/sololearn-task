package com.sololearn.contacts.contact.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sololearn.contacts.R
import com.sololearn.contacts.databinding.ItemRecyclerViewContactBinding
import com.sololearn.contacts.networking.dto.ContactDto

class ContactsRecyclerViewAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {

    var contacts: ArrayList<ContactDto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = DataBindingUtil.inflate<ItemRecyclerViewContactBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_view_contact,
            parent,
            false
        )

        return ViewHolder(
            itemView,
            onClickListener
        )
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindContact(contacts[position])
    }

    fun updateContact(contactDto: ContactDto) {
        var index = 0
        contacts.forEach {
            if (contactDto.id == it.id) {
                contacts[index] = contactDto
                notifyItemChanged(index)
                return
            }
            index++
        }
    }

    fun deleteContact(contactDto: ContactDto) {
        notifyItemRemoved(contacts.indexOf(contactDto))
        contacts.remove(contactDto)
    }

    class ViewHolder(
        private val itemBinding: ItemRecyclerViewContactBinding,
        private val onClickListener: OnClickListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var contact: ContactDto

        init {
            itemBinding.imageViewEdit.setOnClickListener {
                onClickListener.onEditClick(contact)
            }
            itemBinding.imageViewDelete.setOnClickListener {
                onClickListener.onDeleteClick(contact)
            }
        }

        internal fun bindContact(contactDto: ContactDto) {
            contact = contactDto
            itemBinding.contact = contactDto
        }
    }

    interface OnClickListener {
        fun onEditClick(contact: ContactDto)
        fun onDeleteClick(contact: ContactDto)
    }
}