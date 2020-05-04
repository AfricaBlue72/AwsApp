package com.example.awsapp.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface  BaseAuthProvider {
    val userName: MutableLiveData<String>
    val currentUserState: MutableLiveData<AuthStatus>

    fun signup(userName: String,
             password: String,
             userAttributes: MutableMap<String, String>): AuthResult

    fun confirmSignup (userName: String, code: String): AuthResult
    fun signin(userName: String, password: String): AuthResult

    fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean): AuthResult
}