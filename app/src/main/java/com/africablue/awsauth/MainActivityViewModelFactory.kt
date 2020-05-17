package com.africablue.awsauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africablue.awsauth.authproviders.BaseAuthProvider

class MainActivityViewModelFactory(
    private val authProvider: BaseAuthProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(authProvider) as T
    }
}