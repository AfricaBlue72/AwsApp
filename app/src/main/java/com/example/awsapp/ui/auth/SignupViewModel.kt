package com.example.awsapp.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.PrimaryKey
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.awsapp.R
import com.example.awsapp.data.AppRepository
import com.example.awsapp.data.CognitoUser
import com.example.awsapp.data.InjectorUtils
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.StringUtils
import kotlinx.android.synthetic.main.auth_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignupViewModel(application: Application) : BaseAuthViewModel(application){
    private val mLogTag = APP_TAG + this::class.java.simpleName

//    private val appRepository: AppRepository
//        get() {
//            val repo = InjectorUtils.getAppRepository(context)
//            return repo
//        }

    fun signup(
        userName: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: String
    ){
        isBusy.value = true
        val userAttributes = mutableMapOf<String, String>()
        userAttributes["email"] = email
        userAttributes["custom:FirstName"] = firstName
        userAttributes["custom:LastName"] = lastName
        userAttributes["custom:Age"] = age

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

        authStatus.postValue(result.status)

        if(result.status == AuthStatus.UNKNOWN || result.status == AuthStatus.ERROR){
            feedback.postValue( context.getString(R.string.auth_message_signup_nok) )
        }
        else{
            feedback.postValue( context.getString(R.string.auth_message_signin_ok) )
        }
    }
//    fun signup(
//        userName: String,
//        email: String,
//        password: String,
//        firstName: String,
//        lastName: String,
//        age: String
//    ) {
//        isBusy.value = true
//        val userAttributes = mutableMapOf<String, String>()
//        userAttributes["email"] = email
//        userAttributes["custom:FirstName"] = firstName
//        userAttributes["custom:LastName"] = lastName
//        userAttributes["custom:Age"] = age
//
//        AWSMobileClient.getInstance().signUp(
//            userName,
//            password,
//            userAttributes,
//            null,
//            object : Callback<SignUpResult> {
//                override fun onResult(result: SignUpResult?) {
//                    isBusy.postValue(false)
//                    Log.i(mLogTag, "SignUpResult: " + result?.userCodeDeliveryDetails?.deliveryMedium)
//                    Log.i(mLogTag, "SignUpResult: " + result?.userCodeDeliveryDetails?.attributeName)
//                    Log.i(mLogTag, "SignUpResult: " + result?.userCodeDeliveryDetails?.destination)
//                    Log.i(mLogTag, "SignUpResult: " + result?.userSub)
//                    Log.i(mLogTag, "User: " + AWSMobileClient.getInstance().username)
//                    val details =  AWSMobileClient.getInstance().currentUserState().details
//                    Log.i(mLogTag, "Signup succesful")
//
//                    val user = CognitoUser(
//                        result?.userSub,
//                        userName,
//                        StringUtils.obscureEmail(email),
//                        firstName,
//                        lastName,
//                        age,
//                        AuthStatus.SIGNED_UP_WAIT_FOR_CODE)
//
//                    authStatus.postValue(AuthStatus.SIGNED_IN_WAIT_FOR_CODE)
//                }
//
//                override fun onError(e: Exception?) {
//                    isBusy.postValue(false)
//                    authStatus.postValue(AuthStatus.SIGNED_UP_NOK)
//                    feedback.postValue(context.getString(R.string.auth_message_signup_nok))
//                    Log.e(mLogTag, "Signup error.", e)
//                }
//            })
//    }

    fun navigateToConfirmCode(navController: NavController, userName: String){
        TODO ("Remove navigating in ViewModel")
        authStatus.value = AuthStatus.CONFIRMING_CODE
        val action = ConfirmCodeFragmentDirections.actionGlobalConfirmCodeFragment(
            userName , 1)
        navController.navigate(action)
    }
}