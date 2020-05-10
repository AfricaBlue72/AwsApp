package com.example.awsapp

import androidx.lifecycle.ViewModel
import com.example.awsapp.authproviders.BaseAuthProvider
import com.example.awsapp.util.APP_TAG

class MainActivityViewModel (authProvider: BaseAuthProvider): ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    val currentUserState = authProvider.currentUserState
    val userName = authProvider.userName

}