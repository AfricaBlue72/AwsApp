package com.example.awsapp.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import androidx.lifecycle.liveData
import com.amazonaws.mobile.client.SignOutOptions
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails
import com.example.awsapp.data.AppRepository
import com.example.awsapp.data.CognitoUser
import com.example.awsapp.data.CognitoUserDao
import com.example.awsapp.util.StringUtils

class AwsAuthProvider() : BaseAuthProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName

    override fun signin(userName: String, password: String): AuthResult  {
        val result: AuthResult = AuthResult()

        try {
            val signInResult = AWSMobileClient.getInstance().signIn(
                userName,
                password,
                null
            )
            when(signInResult.signInState){
                SignInState.NEW_PASSWORD_REQUIRED -> {
                    result.status = AuthStatus.NEW_PASSWORD_REQUIRED
                }
                SignInState.DONE -> {
                    result.status = AuthStatus.SIGNED_IN
                }
                SignInState.SMS_MFA -> {
                    result.status = AuthStatus.SIGNED_IN_WAIT_FOR_CODE
                }
            }
            result.providerResult = signInResult
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            return result
        }
    }

    fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean): AuthResult{
        val result: AuthResult = AuthResult()

        try {
            AWSMobileClient.getInstance().signOut(
                SignOutOptions.builder()
                    .signOutGlobally(signOutGlobally)
                    .invalidateTokens(invalidateTokens)
                    .build())
            result.status = AuthStatus.SIGNED_OUT
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            return result
        }
    }

    fun signup(userName: String,
              password: String,
              userAttributes: MutableMap<String, String>): AuthResult{
        val result: AuthResult = AuthResult()

        try {
            val signUpResult = AWSMobileClient.getInstance().signUp(
                userName,
                password,
                userAttributes,
                null
            )
            if(signUpResult.confirmationState == true){
                result.status = AuthStatus.SIGNED_UP
            }else{
//                TODO("Take UserCodeDeliveryDetails into account")
                result.status = AuthStatus.SIGNED_UP_WAIT_FOR_CODE
            }
            result.providerResult = signUpResult
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            return result
        }
    }

    fun confirmSignup (userName: String, code: String): AuthResult{
        val result: AuthResult = AuthResult()

        try {
            val signUpResult = AWSMobileClient.getInstance().confirmSignUp(userName, code)
            if(signUpResult.confirmationState == true){
                result.status = AuthStatus.SIGNED_UP
            }else{
//                TODO("Take UserCodeDeliveryDetails into account")
                result.status = AuthStatus.SIGNED_UP_WAIT_FOR_CODE
            }
            result.providerResult = signUpResult
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            return result
        }
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