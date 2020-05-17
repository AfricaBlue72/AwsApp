package com.africablue.awsapp.authproviders

import android.content.Context
import com.africablue.awsapp.ui.auth.*

object AuthInjectorUtils {
    private fun getAwsAuthRepository(): BaseAuthProvider{
        return ProviderInjector.getAwsAuthProvider()
    }
    fun provideSigninViewModelFactory (context: Context): SigninViewModelFactory
    {
        return SigninViewModelFactory(context,
            getAwsAuthRepository()
        )
    }
    fun provideSignoutViewModelFactory (context: Context): SignoutViewModelFactory
    {
        return SignoutViewModelFactory(context,
            getAwsAuthRepository()
        )
    }
    fun provideSignupViewModelFactory (context: Context): SignupViewModelFactory
    {
        return SignupViewModelFactory(context,
            getAwsAuthRepository()
        )
    }

    fun provideMainActivityViewModelFactory(): MainActivityViewModelFactory
    {
        return MainActivityViewModelFactory(getAwsAuthRepository())
    }

    fun provideFlowDialogViewModelFactory(context: Context): FlowDialogViewModelFactory
    {
        return FlowDialogViewModelFactory(context, getAwsAuthRepository())
    }

    fun provideForgotPasswordViewModelFactory(context: Context): ForgotPasswordViewModelFactory
    {
        return ForgotPasswordViewModelFactory(context, getAwsAuthRepository())
    }

    fun provideViewTokensViewModelFactory(context: Context): ViewTokensViewModelFactory
    {
        return ViewTokensViewModelFactory(context, getAwsAuthRepository())
    }
    fun provideChangePasswordViewModelFactory (context: Context): ChangePasswordViewModelFactory
    {
        return ChangePasswordViewModelFactory(context,
            getAwsAuthRepository()
        )
    }
}