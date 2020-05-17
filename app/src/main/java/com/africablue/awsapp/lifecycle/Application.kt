package com.africablue.awsapp.lifecycle

import android.content.Context
import com.amazonaws.mobile.config.AWSConfiguration
import com.africablue.awsapp.authproviders.ProviderInjector
import com.africablue.awsapp.util.APP_TAG


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
        val awsConfig = AWSConfiguration(applicationContext)
        ProviderInjector.getAwsAuthProvider().initialize(applicationContext,awsConfig)

//        AWSMobileClient.getInstance().initialize(applicationContext, object :
//            Callback<UserStateDetails> {
//            override fun onResult(result: UserStateDetails?) {
//                Log.i(mLogTag, "onResult: " + result?.userState)
//            }
//
//            override fun onError(e: Exception?) {
//                Log.e(mLogTag, "Initialization error.", e)
//            }
//        })
    }

    fun getAppContext(): Context? {
        return applicationContext
    }
}