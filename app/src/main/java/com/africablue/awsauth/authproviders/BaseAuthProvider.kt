package com.africablue.awsauth.authproviders

import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.results.Tokens

interface  BaseAuthProvider {
    val userName: MutableLiveData<String>
    val currentUserState: MutableLiveData<AuthStatus>
    val forgotPasswordState: MutableLiveData<ForgotPasswordStatus>
    val changePasswordState: MutableLiveData<ChangePasswordStatus>

    fun updateUserName()

    fun getTokens(): Tokens?

    fun signup(userName: String,
             password: String,
             userAttributes: MutableMap<String, String>): AuthResult

    fun resendSignup(userName: String) : AuthResult

    fun confirmSignup (userName: String, code: String): AuthResult

    fun signin(userName: String, password: String): AuthResult

    fun confirmSignin( signInChallengeResponse: String): AuthResult

    fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean): AuthResult

    fun forgotPassword(userName: String): AuthResult

    fun confirmForgotPassword(userName: String, code: String): AuthResult

    fun changePassword(oldPassword: String, newPassword: String): AuthResult

    fun refresh()
}