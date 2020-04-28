package com.example.awsapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG

abstract class BaseAuthViewModel (application: Application) : AndroidViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName
    protected val context =  application.applicationContext

    var isBusy = MutableLiveData<Boolean>().apply{
        value = false
    }
    var authStatus = MutableLiveData<AuthStatus>().apply{
        value = AuthStatus.UNKNOWN
    }

    var feedback = MutableLiveData<String?>()
}