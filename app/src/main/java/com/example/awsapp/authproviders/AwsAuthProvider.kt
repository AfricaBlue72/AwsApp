package com.example.awsapp.authproviders

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG
import java.lang.Exception
import com.amazonaws.mobile.client.SignOutOptions
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.results.ForgotPasswordState
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.services.cognitoidentityprovider.model.PasswordResetRequiredException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.example.awsapp.lifecycle.Application

class AwsAuthProvider() : BaseAuthProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName

    override val userName = MutableLiveData<String>().apply {
        value = Application.applicationContext().getString(R.string.auth_guest_user)
    }
    override val currentUserState = MutableLiveData<AuthStatus>().apply {
        value = AuthStatus.UNKNOWN
    }
    override val forgotPasswordState = MutableLiveData<ForgotPasswordStatus>().apply{
        value = ForgotPasswordStatus.UNKNOWN
}

    init{
        updateState()
    }

    private fun updateCurrentUserState() {
        val result = when (AWSMobileClient.getInstance().currentUserState().userState){
            UserState.GUEST -> AuthStatus.GUEST
            UserState.SIGNED_IN -> AuthStatus.SIGNED_IN
            UserState.SIGNED_OUT -> AuthStatus.SIGNED_OUT
            UserState.SIGNED_OUT_FEDERATED_TOKENS_INVALID -> AuthStatus.SIGNED_OUT
            UserState.SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> AuthStatus.SIGNED_OUT
            UserState.UNKNOWN -> AuthStatus.UNKNOWN
            else -> AuthStatus.UNKNOWN
        }
        currentUserState.postValue(result)
    }

    private fun updateUserName() {
        val name = AWSMobileClient.getInstance().username ?:
                Application.applicationContext().getString(R.string.auth_guest_user)
        userName.postValue(name)
    }

    private fun updateState(){
        updateCurrentUserState()
        updateUserName()
    }

    override fun signup(userName: String,
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
            if(signUpResult.confirmationState == false){
                result.status = AuthStatus.SIGNED_UP_WAIT_FOR_CODE
            }else{
                result.status = AuthStatus.SIGNED_UP
            }
            result.providerResult = signUpResult
            this.userName.postValue(userName)
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            this.currentUserState.postValue(result.status)
            return result
        }
    }

    override fun resendSignup(userName: String): AuthResult {
        val result: AuthResult = AuthResult()

        try {
            val signUpResult = AWSMobileClient.getInstance().resendSignUp(
                userName
            )
            if(signUpResult.confirmationState == false){
                result.status = AuthStatus.SIGNED_UP_WAIT_FOR_CODE
            }else{
                result.status = AuthStatus.SIGNED_UP
            }
            result.providerResult = signUpResult
            this.userName.postValue(userName)
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            this.currentUserState.postValue(result.status)
            return result
        }
    }

    override fun confirmSignup (userName: String, code: String): AuthResult{
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
            this.userName.postValue(userName)
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            this.currentUserState.postValue(result.status)
            return result
        }
    }
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
            this.userName.postValue(userName)
        }
        catch (e: UserNotConfirmedException){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.SIGNED_UP_WAIT_FOR_CODE
            result.message = e.message
        }
        catch (e: PasswordResetRequiredException){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.NEW_PASSWORD_REQUIRED
            result.message = e.message
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            this.currentUserState.postValue(result.status)
            return result
        }
    }

    override fun signout(signOutGlobally: Boolean, invalidateTokens: Boolean): AuthResult{
        val result: AuthResult = AuthResult()

        try {
            AWSMobileClient.getInstance().signOut(
                SignOutOptions.builder()
                    .signOutGlobally(signOutGlobally)
                    .invalidateTokens(invalidateTokens)
                    .build())
            result.status = AuthStatus.SIGNED_OUT
            userName.postValue(Application.applicationContext().getString(R.string.auth_guest_user))
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.status = AuthStatus.ERROR
            result.message = e.message
        }
        finally {
            this.currentUserState.postValue(result.status)
            return result
        }
    }

    override fun forgotPassword(userName: String): AuthResult {
        val result: AuthResult = AuthResult()

        try {
            val forgotPasswordResult = AWSMobileClient.getInstance().forgotPassword(userName)
            when(forgotPasswordResult.state){
                ForgotPasswordState.CONFIRMATION_CODE -> result.forgotPasswordStatus = ForgotPasswordStatus.CONFIRM
                ForgotPasswordState.DONE -> result.forgotPasswordStatus = ForgotPasswordStatus.DONE
            else-> result.forgotPasswordStatus = ForgotPasswordStatus.UNKNOWN
            }
            result.providerResult = forgotPasswordResult
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.forgotPasswordStatus = ForgotPasswordStatus.ERROR
        }
        finally {
            this.forgotPasswordState.postValue(result.forgotPasswordStatus)
            return result
        }
    }

    override fun confirmForgotPassword(userName: String, code: String): AuthResult {
        val result: AuthResult = AuthResult()

        try {
            val forgotPasswordResult = AWSMobileClient.getInstance().confirmForgotPassword(userName, code)
            when(forgotPasswordResult.state){
                ForgotPasswordState.CONFIRMATION_CODE -> result.forgotPasswordStatus = ForgotPasswordStatus.CONFIRM
                ForgotPasswordState.DONE -> result.forgotPasswordStatus = ForgotPasswordStatus.DONE
                else-> result.forgotPasswordStatus = ForgotPasswordStatus.UNKNOWN
            }
            result.providerResult = forgotPasswordResult
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.message = e.message
            result.forgotPasswordStatus = ForgotPasswordStatus.ERROR
        }
        finally {
            this.forgotPasswordState.postValue(result.forgotPasswordStatus)
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