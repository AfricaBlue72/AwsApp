package com.africablue.awsauth.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africablue.awsauth.authproviders.BaseAuthProvider

class FlowDialogViewModelFactory(
    private val context: Context,
    private val authProvider: BaseAuthProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FlowDialogViewModel(context, authProvider) as T
    }
}