package com.africablue.awsapp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://docs.aws.amazon.com/"
    }
    val url: LiveData<String> = _url
}