package com.africablue.awsapp.util

import androidx.fragment.app.Fragment
import com.africablue.awsapp.authproviders.AwsAuthProvider
import com.africablue.awsapp.lifecycle.Application
import com.africablue.awsapp.translateprovider.AwsTranslateProvider
import com.amazonaws.auth.AWSCredentials

fun Fragment.getViewModelFactoryForAuthRepo(): AuthViewModelFactory {
    //val authProvider =  ProviderInjector.getAwsAuthProvider()
    val authProvider = (requireContext().applicationContext as Application).authProvider
    return AuthViewModelFactory(
        this.requireContext(),
        authProvider
    )
}

fun Fragment.getViewModelFactoryForTranslateChat(): TranslateViewModelFactory {
    val authProvider = (requireContext().applicationContext as Application).authProvider
    val credentials = authProvider.getCredentials()

    if(credentials is AWSCredentials){
        return TranslateViewModelFactory(
            this.requireContext(),
            credentials as AWSCredentials,
            AwsTranslateProvider
        )}
    else{
        throw IllegalArgumentException("Unknown credentials provider.")
    }
}