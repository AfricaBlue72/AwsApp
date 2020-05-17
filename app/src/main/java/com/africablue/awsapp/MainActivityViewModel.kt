package com.africablue.awsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.africablue.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel (val authProvider: BaseAuthProvider): ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    val currentUserState = authProvider.currentUserState
    val userName = authProvider.userName
}