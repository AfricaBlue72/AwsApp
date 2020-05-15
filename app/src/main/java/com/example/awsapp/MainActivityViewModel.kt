package com.example.awsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awsapp.authproviders.BaseAuthProvider
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel (val authProvider: BaseAuthProvider): ViewModel(){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    val currentUserState = authProvider.currentUserState
    val userName = authProvider.userName

    fun updateUserName() {
        viewModelScope.launch {
            _updateUserName()
        }
    }

    private suspend fun _updateUserName() = withContext(Dispatchers.IO){
        authProvider.getTokens()
    }

}