package com.africablue.awsauth.ui.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.africablue.awsauth.util.APP_TAG
import com.africablue.awsauth.R
import com.africablue.awsauth.authproviders.*
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

    fun confirmSignin(challengeResonse: String){
        isBusy.value = true

        viewModelScope.launch {
            _confirmSignin(challengeResonse)
            isBusy.postValue(false)
        }
    }

    private suspend fun _confirmSignin(challengeResonse: String) = withContext(Dispatchers.IO){

        val result = authProvider.confirmSignin(challengeResonse)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signin_nok) )
        }
        else{
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signin_ok) )
        }
    }

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


        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_ok) )
        }
    }
}