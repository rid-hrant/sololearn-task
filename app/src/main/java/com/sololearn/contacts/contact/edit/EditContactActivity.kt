package com.sololearn.contacts.contact.edit

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sololearn.contacts.R
import com.sololearn.contacts.contact.base.BaseModifyActivity
import com.sololearn.contacts.contact.edit.viewmodel.EditContactViewModel
import com.sololearn.contacts.databinding.ActivityBaseModifyBinding
import com.sololearn.contacts.networking.dto.ContactDto

class EditContactActivity : BaseModifyActivity<ActivityBaseModifyBinding>() {

    private lateinit var editContactViewModel: EditContactViewModel

    private val contactDto: ContactDto
        get() {
            return intent?.extras?.getParcelable(EXTRA_KEY_CONTACT) as ContactDto
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.buttonDone.text = getString(R.string.save)
        binding.contact = contactDto
        editContactViewModel = ViewModelProviders.of(this).get(EditContactViewModel::class.java)

        editContactViewModel.editedContact.observe(this, Observer {
            finish()
        })

        editContactViewModel.error.observe(this, Observer {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        })
    }

    override fun View.onDoneClick() {
        val newContactDto = contactDto.copy()
        newContactDto.firstName = binding.editTextFirstName.text.toString()
        newContactDto.lastName = binding.editTextLastName.text.toString()
        newContactDto.phoneNumber = binding.editTextPhoneNumber.text.toString()

        if (contactDto == newContactDto) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_KEY_CONTACT, newContactDto)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)

            editContactViewModel.editContact(newContactDto)
        }
    }

    companion object {
        const val EXTRA_KEY_CONTACT = "EXTRA_KEY_CONTACT"
    }
}
