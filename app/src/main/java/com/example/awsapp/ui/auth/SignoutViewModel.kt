package com.example.awsapp.ui.auth

import android.app.Application
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import java.lang.Exception

class SignoutViewModel (application: Application) : BaseAuthViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun simpleSignout(signOutGlobally: Boolean, invalidateTokens: Boolean){
        isBusy.value = true
        val awsClient = AWSMobileClient.getInstance()

        awsClient.signOut(
            SignOutOptions.builder()
                .signOutGlobally(signOutGlobally)
                .invalidateTokens(invalidateTokens)
                .build(),
            object : Callback<Void>{
                override fun onResult(result: Void?) {
                    feedback.postValue( context.getString(R.string.auth_message_signout_ok) )
                    isBusy.postValue(false)
                    authStatus.postValue(AuthStatus.SIGNED_OUT)

                }
                override fun onError(e: Exception?) {
                    feedback.postValue( context.getString(R.string.auth_message_signout_nok) )
                    isBusy.postValue(false)

                }
        })
    }
}