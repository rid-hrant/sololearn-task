package com.sololearn.contacts.networking

import com.sololearn.contacts.networking.dto.ContactDto
import retrofit2.Call
import retrofit2.http.*

interface SoloLearnService {

    @GET("/Contacts/list")
    fun getContacts(
        @Query("orderBy") orderBy: String? = null,
        @Query("index") index: Int? = null,
        @Query("count") count: Int? = null
    ): Call<List<ContactDto>>

    @GET("/Contacts")
    fun getContact(@Query("id") id: Int): Call<ContactDto>

    @POST("/Contacts")
    fun postContact(@Body contactDto: ContactDto): Call<String>

    @DELETE("/Contacts")
    fun deleteContact(@Query("id") id: Int): Call<Boolean>
}