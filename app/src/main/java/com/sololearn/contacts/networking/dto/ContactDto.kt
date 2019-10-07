package com.sololearn.contacts.networking.dto

import android.os.Parcel
import android.os.Parcelable

data class ContactDto(
    var id: Int?,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var creationDate: String
) : Parcelable {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
        parcel.writeString(creationDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactDto> {
        override fun createFromParcel(parcel: Parcel): ContactDto {
            return ContactDto(parcel)
        }

        override fun newArray(size: Int): Array<ContactDto?> {
            return arrayOfNulls(size)
        }
    }
}