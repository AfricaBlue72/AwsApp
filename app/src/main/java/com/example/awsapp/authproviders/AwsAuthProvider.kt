package com.example.awsapp.authproviders

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.*
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG
import java.lang.Exception
import com.amazonaws.mobile.client.results.ForgotPasswordState
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.Tokens
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.services.cognitoidentityprovider.model.PasswordResetRequiredException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.example.awsapp.lifecycle.Application

object AwsAuthProvider : BaseAuthProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName
    lateinit var context: Context
    lateinit var awsConfig: AWSConfiguration

//    companion object {
//        // For Singleton instantiation
//        @Volatile private var instance: AwsAuthProvider? = null
//
//        fun getInstance(context: Context, awsConfig: AWSConfiguration) =
//            instance ?: synchronized(this) {
//                instance ?: AwsAuthProvider(context, awsConfig).also { instance = it }
//            }
//    }

    fun initialize(context: Context, awsConfig: AWSConfiguration){
        this.context = context
        this.awsConfig = awsConfig
        AWSMobileClient.getInstance().initialize(this.context, this.awsConfig, object :
            Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                Log.i(mLogTag, "onResult: " + result?.userState)
                updateState()
            }

            override fun onError(e: Exception?) {
                Log.e(mLogTag, "Initialization error.", e)
            }
        })
    }

    override val userName = MutableLiveData<String>().apply {
        //value = Application.applicationContext().getString(R.string.auth_guest_user)
        value = AWSMobileClient.getInstance().username
    }
    override val currentUserState = MutableLiveData<AuthStatus>().apply {
        value = AuthStatus.UNKNOWN
    }
    override val forgotPasswordState = MutableLiveData<ForgotPasswordStatus>().apply{
        value = ForgotPasswordStatus.UNKNOWN
    }
    override val changePasswordState = MutableLiveData<ChangePasswordStatus>().apply{
        value = ChangePasswordStatus.UNKNOWN
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
        Log.i(mLogTag, "Current user state: " + result.toString())
        currentUserState.postValue(result)
    }

    override fun updateUserName() {
        val name = AWSMobileClient.getInstance().username ?:
                this.context.getString(R.string.auth_guest_user)
        userName.postValue(name)
    }

    private fun updateState(){
        updateCurrentUserState()
        updateUserName()
    }

    override fun getTokens(): Tokens? {
        return AWSMobileClient.getInstance().getTokens()
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

    override fun confirmSignin( signInChallengeResponse: String): AuthResult{
        val result: AuthResult = AuthResult()

        try {
            val signInResult = AWSMobileClient.getInstance().confirmSignIn(
                signInChallengeResponse
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
            updateUserName()
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
            updateUserName()
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
            updateUserName()
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

    override fun confirmForgotPassword(password: String, code: String): AuthResult {
        val result: AuthResult = AuthResult()

        try {
            val forgotPasswordResult = AWSMobileClient.getInstance().confirmForgotPassword(password, code)
            when(forgotPasswordResult.state){
                ForgotPasswordState.CONFIRMATION_CODE -> result.forgotPasswordStatus = ForgotPasswordStatus.CONFIRM
                ForgotPasswordState.DONE -> result.forgotPasswordStatus = ForgotPasswordStatus.DONE
                else-> result.forgotPasswordStatus = ForgotPasswordStatus.UNKNOWN
            }
            result.providerResult = forgotPasswordResult
            updateUserName()
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

    override fun changePassword(oldPassword: String, newPassword: String): AuthResult {
        val result: AuthResult = AuthResult()

        try {
            AWSMobileClient.getInstance().changePassword(oldPassword, newPassword)
            result.changePasswordStatus = ChangePasswordStatus.DONE
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
            result.message = e.message
            result.changePasswordStatus = ChangePasswordStatus.ERROR
        }
        finally {
            this.changePasswordState.postValue(result.changePasswordStatus)
            return result
        }
    }

    override fun refresh() {
        try {
            AWSMobileClient.getInstance().refresh()
            updateUserName()
            updateCurrentUserState()
            this.changePasswordState.postValue(ChangePasswordStatus.UNKNOWN)
            Log.i(mLogTag, "Refreshed user. ")
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
        }
    }
}