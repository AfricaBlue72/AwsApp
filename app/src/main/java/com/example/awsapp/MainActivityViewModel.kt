package com.example.awsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.providers.BaseAuthProvider
import com.example.awsapp.providers.ProviderInjector
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel (authProvider: BaseAuthProvider): ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    val currentUserState = authProvider.currentUserState
    val userName = authProvider.userName

}