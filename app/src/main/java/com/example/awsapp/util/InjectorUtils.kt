package com.example.awsapp.util

import android.app.Application
import android.content.Context
import com.example.awsapp.providers.BaseAuthProvider
import com.example.awsapp.providers.ProviderInjector
import com.example.awsapp.ui.auth.*

object InjectorUtils {
    private fun getAwsAuthRepository(): BaseAuthProvider{
        return ProviderInjector.getAwsAuthProvider()
    }

    fun provideConfirmCodeViewModelFactory (context: Context): ConfirmCodeViewModelFactory
    {
        return ConfirmCodeViewModelFactory(context, getAwsAuthRepository())
    }
    fun provideSigninViewModelFactory (context: Context): SigninViewModelFactory
    {
        return SigninViewModelFactory(context, getAwsAuthRepository())
    }
    fun provideSignoutViewModelFactory (context: Context): SignoutViewModelFactory
    {
        return SignoutViewModelFactory(context, getAwsAuthRepository())
    }
    fun provideSignupViewModelFactory (context: Context): SignupViewModelFactory
    {
        return SignupViewModelFactory(context, getAwsAuthRepository())
    }

    fun provideMainActivityViewModelFactory(): MainActivityViewModelFactory
    {
        return MainActivityViewModelFactory(getAwsAuthRepository())
    }
}