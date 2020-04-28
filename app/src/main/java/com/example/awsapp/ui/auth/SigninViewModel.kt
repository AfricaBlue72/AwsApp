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

    private val authProvider: AwsAuthProvider
    get() {
        val authProvider = ProviderInjector.getAwsAuthProvider()
        return authProvider
    }

    fun signin(userName: String, password: String){
        isBusy.value = true

        viewModelScope.launch {
            _singin(userName, password)
        }
    }

    private suspend fun _singin(userName: String, password: String) = withContext(Dispatchers.IO){
        val result = authProvider.signin(userName, password)
        if(result == null || result.signInState == SignInState.UNKNOWN){
            authStatus.postValue(AuthStatus.SIGNED_IN_NOK)
            feedback.postValue( context.getString(R.string.auth_message_signin_nok) )
        }
        else{
            authStatus.postValue(AuthStatus.SIGNED_IN)
            feedback.postValue( context.getString(R.string.auth_message_signin_ok) )
        }

        isBusy.postValue(false)

//        isBusy.value = true
//        val result = authProvider.signin(userName, password)
//
//        authStatus.value = result?.status ?: AuthStatus.UNKNOWN
//        if(authStatus.value == AuthStatus.SIGNED_IN){
//            feedback.value = context.getString(R.string.auth_message_signin_ok)
//        }
//        else {
//            feedback.value = context.getString(R.string.auth_message_signin_nok)
//        }
//        isBusy.value = false
    }

//    fun signin(userName: String, password: String){
//        isBusy.value = true
//        AWSMobileClient.getInstance().signIn(
//            userName,
//            password,
//            null,
//            object : Callback<SignInResult> {
//                override fun onResult(result: SignInResult?) {
//                    isBusy.postValue(false)
//                    authStatus.postValue(AuthStatus.SIGNED_IN)
//                    feedback.postValue( context.getString(R.string.auth_message_signin_ok) )
//                    Log.i(mLogTag, "Signin successful.")
//                }
//
//                override fun onError(e: Exception?) {
//                    isBusy.postValue(false)
//                    authStatus.postValue(AuthStatus.SIGNED_IN_NOK)
//                    feedback.postValue(context.getString(R.string.auth_message_signin_nok))
//
//                    Log.e(mLogTag, "Signin error.", e)
//                }
//            }
//        )
//    }
}