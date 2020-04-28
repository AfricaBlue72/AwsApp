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
        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            authStatus.postValue(result.status)
            feedback.postValue( context.getString(R.string.auth_message_signin_nok) )
        }
        else{
            authStatus.postValue(AuthStatus.SIGNED_OUT)
            feedback.postValue( context.getString(R.string.auth_message_signin_ok) )
        }
    }
}