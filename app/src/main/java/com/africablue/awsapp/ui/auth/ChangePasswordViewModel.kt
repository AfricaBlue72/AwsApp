package com.africablue.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.R
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.africablue.awsapp.authproviders.ChangePasswordStatus
import com.africablue.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel (context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context,authProvider){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun changePassword(oldPassword: String, newPassword: String){
        isBusy.value = true

        viewModelScope.launch {
            _changePassword(oldPassword, newPassword)
            isBusy.postValue(false)
        }
    }

    private suspend fun _changePassword(oldPassword: String, newPassword: String) = withContext(
        Dispatchers.IO){

        val result = authProvider.changePassword(oldPassword, newPassword)

        if(result.changePasswordStatus == ChangePasswordStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_change_password_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_change_password_ok) )
        }
    }

    fun refresh(){
        isBusy.value = true

        viewModelScope.launch {
            _refresh()
            isBusy.postValue(false)
        }
    }

    private suspend fun _refresh() = withContext(
        Dispatchers.IO){

        authProvider.refresh()
    }
}