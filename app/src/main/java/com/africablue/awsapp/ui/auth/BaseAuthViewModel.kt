package com.africablue.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.africablue.awsapp.util.APP_TAG

abstract class BaseAuthViewModel(context: Context, authProvider: BaseAuthProvider) : ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName
    protected val context =  context
    protected val authProvider = authProvider

    var isBusy = MutableLiveData<Boolean>().apply{
        value = false
    }

    var feedback = MutableLiveData<String?>()

    val userName = authProvider.userName
    val authStatus = authProvider.currentUserState
    val forgotPasswordStatus = authProvider.forgotPasswordState
    val changePasswordStatus = authProvider.changePasswordState

//    protected val authProvider: AwsAuthProvider
//        get() {
//          val authProvider = ProviderInjector.getAwsAuthProvider()
//            return authProvider
//            //            TODO("Use injection to insert correct provider")
//            //            TODO("Set type to BaseAuthProvider")
//
//        }
}