package com.example.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.awsapp.authproviders.BaseAuthProvider

class ForgotPasswordViewModelFactory(
    private val context: Context,
    private val authProvider: BaseAuthProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(context, authProvider) as T
    }
}