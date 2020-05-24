package com.africablue.awsapp.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africablue.awsapp.MainActivityViewModel
import com.africablue.awsapp.authproviders.BaseAuthProvider
import com.africablue.awsapp.ui.auth.*

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory constructor(
    private val context: Context,
    private val baseAuthProvider: BaseAuthProvider
    ): ViewModelProvider.NewInstanceFactory(){


    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = with(modelClass) {
        when {
            isAssignableFrom(MainActivityViewModel::class.java) ->
                MainActivityViewModel(baseAuthProvider)
            isAssignableFrom(ChangePasswordViewModel::class.java) ->
                ChangePasswordViewModel(context, baseAuthProvider)
            isAssignableFrom(FlowDialogViewModel::class.java) ->
                FlowDialogViewModel(context, baseAuthProvider)
            isAssignableFrom(ForgotPasswordViewModel::class.java) ->
                ForgotPasswordViewModel(context, baseAuthProvider)
            isAssignableFrom(SigninViewModel::class.java) ->
                SigninViewModel(context, baseAuthProvider)
            isAssignableFrom(SignoutViewModel::class.java) ->
                SignoutViewModel(context, baseAuthProvider)
            isAssignableFrom(SignupViewModel::class.java) ->
                SignupViewModel(context, baseAuthProvider)
            isAssignableFrom(ViewTokensViewModel::class.java) ->
                ViewTokensViewModel(context, baseAuthProvider)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}