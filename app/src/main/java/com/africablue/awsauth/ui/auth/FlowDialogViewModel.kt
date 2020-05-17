package com.africablue.awsauth.ui.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.africablue.awsauth.R
import com.africablue.awsauth.authproviders.AuthStatus
import com.africablue.awsauth.authproviders.BaseAuthProvider
import com.africablue.awsauth.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlowDialogViewModel (context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context ,authProvider){

    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun resendSignup(userName: String){
        isBusy.value = true

        try{
            viewModelScope.launch {
                _resendSignup(userName)
            }
        }
        finally{
            isBusy.postValue(false)
        }
    }

    private suspend fun _resendSignup(userName: String) = withContext(Dispatchers.IO){

        val result = authProvider.resendSignup(userName)

        //authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signup_resend_nok) )
        }
        else{
            feedback.postValue( context.applicationContext.getString(R.string.auth_message_signup_resend_ok) )
        }
    }
}