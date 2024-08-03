package com.example.greenish.ui.extra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExtraViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is extra Fragment"
    }
    val text: LiveData<String> = _text
}