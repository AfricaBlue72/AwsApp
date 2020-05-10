package com.example.awsapp.authproviders

import androidx.lifecycle.MutableLiveData

interface  BaseAuthProvider {
    val userName: MutableLiveData<String>
    val currentUserState: MutableLiveData<AuthStatus>
    val forgotPasswordState: MutableLiveData<ForgotPasswordStatus>

    fun signup(userName: String,
             password: String,
             userAttributes: MutableMap<String, String>): AuthResult

    fun resendSignup(userName: String) : AuthResult

    fun confirmSignup (userName: String, code: String): AuthResult

    fun signin(userName: String, password: String): AuthResult

    fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean): AuthResult

    fun forgotPassword(userName: String): ForgotPasswordResult

    fun confirmForgotPassword(userName: String, code: String): ForgotPasswordResult
}