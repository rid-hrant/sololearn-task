package com.sololearn.contacts.contact.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sololearn.contacts.R

abstract class BaseModifyActivity<Binding : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_modify)
    }

    abstract fun View.onDoneClick()
}