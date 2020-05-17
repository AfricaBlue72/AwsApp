package com.africablue.awsapp.util

import androidx.fragment.app.Fragment
import com.africablue.awsapp.ViewModelFactory
import com.africablue.awsapp.authproviders.ProviderInjector
import com.africablue.awsapp.lifecycle.Application

fun Fragment.getViewModelFactory(): ViewModelFactory {
    //val authProvider =  ProviderInjector.getAwsAuthProvider()
    val authProvider = (requireContext().applicationContext as Application).authProvider
    return ViewModelFactory(this.requireContext(), authProvider)
}
