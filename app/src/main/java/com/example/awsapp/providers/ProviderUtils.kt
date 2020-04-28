package com.example.awsapp.providers

import android.content.Context

object ProviderInjector {

    fun getAwsAuthProvider(): AwsAuthProvider {
        return AwsAuthProvider.getInstance()
    }
}