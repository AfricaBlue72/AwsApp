package com.example.awsapp.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.AuthStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import androidx.lifecycle.liveData
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails
import com.example.awsapp.data.AppRepository
import com.example.awsapp.data.CognitoUserDao

class AwsAuthProvider() : BaseAuthProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName

    override fun signin(userName: String, password: String): SignInResult?  {
        try {
            return AWSMobileClient.getInstance().signIn(
                userName,
                password,
                null
            )
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            return SignInResult(
                SignInState.UNKNOWN,
                mutableMapOf<String, String>("error" to (e.message ?: "No message")))
        }
    }

    fun signout(ignOutGlobally: Boolean, invalidateTokens: Boolean){

    }

    fun signup(userName: String,
                      email: String,
                      userAttributes: MutableMap<String, String>){

    }

    fun confirmSignup (userName: String, code: String){

    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AwsAuthProvider? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AwsAuthProvider().also { instance = it }
            }
    }
}