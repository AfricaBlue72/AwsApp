package com.example.awsapp.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.providers.BaseAuthProvider
import com.example.awsapp.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel(context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context,authProvider){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    fun signup(
        userName: String,
        email: String,
        password: String
    ){
        isBusy.value = true
        val userAttributes = mutableMapOf<String, String>()
        userAttributes["email"] = email

        viewModelScope.launch {
            _signup(userName,
                password,
                userAttributes)
            isBusy.postValue(false)
        }
    }

    private suspend fun _signup(userName: String,
                                password: String,
                                userAttributes: MutableMap<String, String>) = withContext(Dispatchers.IO){

        val result = authProvider.signup(userName, password, userAttributes)

        //authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_signup_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_signup_ok) )
        }
    }

    fun confirmSignup(code: String){
        confirmSignup(userName.value.toString(), code)
    }

    fun confirmSignup(userName: String, code: String){
        isBusy.value = true

        viewModelScope.launch {
            _confirmSignup(userName, code)
            isBusy.postValue(false)
        }
    }

    private suspend fun _confirmSignup(userName: String, code: String) = withContext(
        Dispatchers.IO){

        val result = authProvider.confirmSignup(userName, code)

        //authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_confirm_code_ok) )
        }
    }

//    fun navigateToConfirmCode(navController: NavController, userName: String){
//        authStatus.value = AuthStatus.CONFIRMING_CODE
//        val action = ConfirmCodeFragmentDirections.actionGlobalConfirmCodeFragment(
//            userName , 1)
//        navController.navigate(action)
//        //TODO ("Remove navigating in ViewModel")
//    }
}