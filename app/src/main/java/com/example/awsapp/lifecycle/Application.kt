package com.example.awsapp.lifecycle

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.config.AWSConfiguration
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG

class Application : android.app.Application()  {
    val mLogTag = APP_TAG + this::class.java.simpleName

    override fun onCreate() {
        super.onCreate()

        AWSMobileClient.getInstance().initialize(applicationContext, object :
            Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                Log.i(mLogTag, "onResult: " + result?.userState)
            }

            override fun onError(e: Exception?) {
                Log.e(mLogTag, "Initialization error.", e)
            }
        }
        )
    }
}