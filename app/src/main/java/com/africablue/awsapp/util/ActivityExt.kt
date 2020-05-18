package com.africablue.awsapp.util

import androidx.appcompat.app.AppCompatActivity
import com.africablue.awsapp.ViewModelFactory
import com.africablue.awsapp.authproviders.ProviderInjector
import com.africablue.awsapp.lifecycle.Application

fun AppCompatActivity.getViewModelFactoryForAuthRepo(): ViewModelFactory {
    val authProvider = (applicationContext as Application).authProvider
    return ViewModelFactory(this.applicationContext, authProvider)
}