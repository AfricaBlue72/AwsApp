package com.africablue.awsapp.util

import androidx.appcompat.app.AppCompatActivity
import com.africablue.awsapp.lifecycle.Application

fun AppCompatActivity.getViewModelFactoryForAuthRepo(): AuthViewModelFactory {
    val authProvider = (applicationContext as Application).authProvider
    return AuthViewModelFactory(
        this.applicationContext,
        authProvider
    )
}