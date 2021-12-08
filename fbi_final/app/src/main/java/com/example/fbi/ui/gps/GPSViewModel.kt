package com.example.fbi.ui.gps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GPSViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is GPS Fragment"
    }
    val text: LiveData<String> = _text
}