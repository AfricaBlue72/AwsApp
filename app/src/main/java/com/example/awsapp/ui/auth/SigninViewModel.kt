package com.example.awsapp.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.amazonaws.AmazonServiceException
import com.amazonaws.mobile.client.*
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.example.awsapp.util.APP_TAG
import java.lang.Exception
import com.example.awsapp.R
import com.example.awsapp.providers.AuthResult
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.providers.AwsAuthProvider
import com.example.awsapp.providers.ProviderInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninViewModel(application: Application) : BaseAuthViewModel(application){

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

        authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_signin_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_signin_ok) )
        }
    }
}