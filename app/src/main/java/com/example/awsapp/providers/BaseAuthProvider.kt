package com.example.awsapp.providers

import androidx.lifecycle.LiveData
import java.util.*

//typealias IdentityResponse = (Map<String, String>?) -> Unit
//typealias IdentityHandler = (IdentityRequest, Map<String,String>?, IdentityResponse) -> Unit



interface  BaseAuthProvider {
      fun signin(userName: String, password: String): Any?
//
//    public fun signout(ignOutGlobally: Boolean, invalidateTokens: Boolean): Unit
//
//    public fun signup(userName: String,
//                      email: String,
//                      userAttributes: MutableMap<String, String>): Unit
//
//    fun confirmSignup (userName: String, code: String): Unit

}