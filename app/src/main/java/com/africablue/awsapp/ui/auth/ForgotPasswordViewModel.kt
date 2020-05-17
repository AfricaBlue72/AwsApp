package com.africablue.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.R
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.africablue.awsapp.authproviders.ForgotPasswordStatus
import com.africablue.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordViewModel (context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context ,authProvider){

    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun forgotPassword(userName: String){
        isBusy.value = true

        viewModelScope.launch {
            _forgotPassword(userName)
            isBusy.postValue(false)
        }
    }

    private suspend fun _forgotPassword(userName: String) = withContext(Dispatchers.IO){

        val result = authProvider.forgotPassword(userName)

        if(result.forgotPasswordStatus == ForgotPasswordStatus.ERROR || result.forgotPasswordStatus == ForgotPasswordStatus.UNKNOWN){
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_forgot_password_nok) )
        }
        else{
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_forgot_password_ok) )
        }
    }

    fun confirmForgotPassword(userName: String, code: String){
        isBusy.value = true

        viewModelScope.launch {
            _confirmForgotPassword(userName, code)
            isBusy.postValue(false)
        }
    }

    private suspend fun _confirmForgotPassword(password: String, code: String) = withContext(Dispatchers.IO){

        val result = authProvider.confirmForgotPassword(password, code)

        if(result.forgotPasswordStatus == ForgotPasswordStatus.ERROR || result.forgotPasswordStatus == ForgotPasswordStatus.UNKNOWN){
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_forgot_password_nok) )
        }
        else{
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_forgot_password_ok) )
        }
    }
}