package com.sololearn.contacts.contact.add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sololearn.contacts.networking.APIService
import com.sololearn.contacts.networking.dto.ContactDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddContactViewModel : ViewModel() {

    val addedContact = MutableLiveData<ContactDto>()
    val error = MutableLiveData<Unit>()

    fun addContact(contactDto: ContactDto) {
        APIService.getInstance().postContact(contactDto).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                error.value = Unit
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                addedContact.value = contactDto
            }

        })
    }
}