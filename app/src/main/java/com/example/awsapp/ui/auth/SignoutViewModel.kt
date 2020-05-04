package com.example.awsapp.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.providers.BaseAuthProvider
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignoutViewModel (context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context,authProvider){
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