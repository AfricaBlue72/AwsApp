package com.africablue.awsapp.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africablue.awsapp.authproviders.BaseAuthProvider

class SignupViewModelFactory(
    private val context: Context,
    private val authProvider: BaseAuthProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignupViewModel(context, authProvider) as T
    }
}