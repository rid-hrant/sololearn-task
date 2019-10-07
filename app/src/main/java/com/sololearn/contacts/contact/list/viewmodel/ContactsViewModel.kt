package com.sololearn.contacts.contact.list.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sololearn.contacts.networking.APIService
import com.sololearn.contacts.networking.dto.ContactDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsViewModel(private val payload: Bundle) : ViewModel() {

    val contacts = MutableLiveData<List<ContactDto>>()
    val deleteContact = MutableLiveData<ContactDto>()
    val error = MutableLiveData<Unit>()

    init {
        loadContacts(0)
    }

    fun loadContacts(index: Int) {
        APIService.getInstance().getContacts(
            payload.get(ORDER_BY) as String?, index,
            ITEM_COUNT
        )
            .enqueue(object : Callback<List<ContactDto>> {
                override fun onFailure(call: Call<List<ContactDto>>, t: Throwable) {
                    error.value = Unit
                }

                override fun onResponse(call: Call<List<ContactDto>>, response: Response<List<ContactDto>>) {
                    contacts.value = response.body()
                }
            })
    }

    fun deleteContact(contactDto: ContactDto) {
        contactDto.id?.let {
            APIService.getInstance().deleteContact(it).enqueue(object : Callback<Boolean> {
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    error.value = Unit
                }

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    deleteContact.value = contactDto
                }
            })
        }
    }

    companion object {
        const val ORDER_BY = "ORDER_BY"
        const val ITEM_COUNT = 8
    }
}