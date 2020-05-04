package com.example.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.providers.BaseAuthProvider
import com.example.awsapp.util.APP_TAG

abstract class BaseAuthViewModel(context: Context, authProvider: BaseAuthProvider) : ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName
    protected val context =  context
    protected val authProvider = authProvider

    var isBusy = MutableLiveData<Boolean>().apply{
        value = false
    }
    var authStatus = MutableLiveData<AuthStatus>().apply{
        value = AuthStatus.UNKNOWN
    }

    var feedback = MutableLiveData<String?>()

    val userName = authProvider.userName
    val currentUserState = authProvider.currentUserState

//    protected val authProvider: AwsAuthProvider
//        get() {
//          val authProvider = ProviderInjector.getAwsAuthProvider()
//            return authProvider
//            //            TODO("Use injection to insert correct provider")
//            //            TODO("Set type to BaseAuthProvider")
//
//        }
}