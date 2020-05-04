package com.example.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.R
import com.example.awsapp.providers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninViewModel(context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context ,authProvider){

    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun signin(userName: String, password: String){
        isBusy.value = true

        viewModelScope.launch {
            _singin(userName, password)
            isBusy.postValue(false)
        }
    }

    private suspend fun _singin(userName: String, password: String) = withContext(Dispatchers.IO){

        val result = authProvider.signin(userName, password)

        //authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signin_nok) )
        }
        else{
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signin_ok) )
        }
    }
}