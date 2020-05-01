package com.example.awsapp.ui.auth

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignoutViewModel (application: Application) : BaseAuthViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName


    fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean){
        isBusy.value = true

        viewModelScope.launch {
            _signout(signOutGlobally, invalidateTokens)
            isBusy.postValue(false)
        }
    }

    private suspend fun _signout(signOutGlobally: Boolean, invalidateTokens: Boolean) = withContext(Dispatchers.IO){

        val result = authProvider.signout(signOutGlobally, invalidateTokens)

        authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_signout_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_signout_ok) )
        }
    }
}