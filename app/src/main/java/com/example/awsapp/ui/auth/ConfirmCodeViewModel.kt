package com.example.awsapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.awsapp.R
import com.example.awsapp.data.AppRepository
import com.example.awsapp.data.InjectorUtils
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.AuthStatus
import java.lang.Exception

class ConfirmCodeViewModel (application: Application) : BaseAuthViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun confirmSignup (userName: String, code: String){
        isBusy.postValue(true)

        AWSMobileClient.getInstance().confirmSignUp(
            userName,
            code,
            object : Callback<SignUpResult?>{

                override fun onResult(result: SignUpResult?) {
                    authStatus.postValue(AuthStatus.SIGNED_UP)
                    isBusy.postValue(false)
                    feedback.postValue(context.getString(R.string.auth_message_confirm_code_ok))
                }

                override fun onError(e: Exception?) {
                    authStatus.postValue(AuthStatus.SIGNED_UP_NOK)
                    isBusy.postValue(false)
                    feedback.postValue(context.getString(R.string.auth_message_confirm_code_nok))
                }
            })
    }
}