package com.sololearn.contacts.contact.edit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sololearn.contacts.networking.APIService
import com.sololearn.contacts.networking.dto.ContactDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditContactViewModel : ViewModel() {

    val editedContact = MutableLiveData<String>()
    val error = MutableLiveData<Unit>()

    fun editContact(contactDto: ContactDto) {
        APIService.getInstance().postContact(contactDto).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                error.value = Unit
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                editedContact.value = response.body()
            }
        })
    }
}