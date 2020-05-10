package com.example.awsapp.lifecycle

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amplifyframework.core.Amplify
import com.example.awsapp.util.APP_TAG


class Application : android.app.Application()  {
    val mLogTag = APP_TAG + this::class.java.simpleName

    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

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
        })
    }

    fun getAppContext(): Context? {
        return applicationContext
    }
}