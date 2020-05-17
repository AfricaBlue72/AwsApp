package com.africablue.awsapp.lifecycle

import android.content.Context
import com.africablue.awsapp.authproviders.AwsAuthProvider
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.africablue.awsapp.authproviders.ProviderInjector
import com.africablue.awsapp.util.APP_TAG


class Application : android.app.Application()  {
    val mLogTag = APP_TAG + this::class.java.simpleName
    val authProvider: BaseAuthProvider

    init{
        authProvider = ProviderInjector.getAwsAuthProvider()
    }

    override fun onCreate() {
        super.onCreate()
        val awsConfig = AWSConfiguration(applicationContext)
        if(authProvider is AwsAuthProvider) {
            ProviderInjector.getAwsAuthProvider().initialize(applicationContext, awsConfig)
        }
        else{
            throw IllegalArgumentException("Unknown auth provider")
        }
    }
}