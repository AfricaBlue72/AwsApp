package com.example.awsapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.awsapp.R
import com.example.awsapp.data.AppRepository
import com.example.awsapp.data.InjectorUtils
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ConfirmCodeViewModel (application: Application) : BaseAuthViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun confirmSignup(userName: String, code: String){
        isBusy.value = true

        viewModelScope.launch {
            _confirmSignup(userName, code)
            isBusy.postValue(false)
        }
    }

    private suspend fun _confirmSignup(userName: String, code: String) = withContext(
        Dispatchers.IO){

        val result = authProvider.confirmSignup(userName, code)

        authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_ok) )
        }
    }
}