package com.sololearn.contacts.contact.add

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sololearn.contacts.R
import com.sololearn.contacts.contact.add.viewmodel.AddContactViewModel
import com.sololearn.contacts.contact.base.BaseModifyActivity
import com.sololearn.contacts.databinding.ActivityBaseModifyBinding
import com.sololearn.contacts.networking.dto.ContactDto
import java.text.SimpleDateFormat
import java.util.*

class AddContactActivity : BaseModifyActivity<ActivityBaseModifyBinding>() {

    private lateinit var addContactViewModel: AddContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.buttonDone.text = getString(R.string.create)

        addContactViewModel = ViewModelProviders.of(this).get(AddContactViewModel::class.java)

        addContactViewModel.addedContact.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })

        addContactViewModel.error.observe(this, Observer {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        })
    }

    override fun View.onDoneClick() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

        val contactDto = ContactDto(
            null,
            binding.editTextFirstName.text.trim().toString(),
            binding.editTextLastName.text.trim().toString(),
            binding.editTextPhoneNumber.text.trim().toString(),
            formatter.format(Calendar.getInstance(Locale.getDefault()).timeInMillis)
        )

        if (validContact(contactDto)) {
            addContactViewModel.addContact(contactDto)
        }
    }

    private fun validContact(contactDto: ContactDto): Boolean {
        if (contactDto.firstName.isBlank()) {
            Toast.makeText(this, "Enter your first name.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (contactDto.lastName.isBlank()) {
            Toast.makeText(this, "Enter your last name.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (contactDto.phoneNumber.isBlank()) {
            Toast.makeText(this, "Enter your phone number.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
