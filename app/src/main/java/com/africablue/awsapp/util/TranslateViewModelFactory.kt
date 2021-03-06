package com.africablue.awsapp.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africablue.awsapp.authproviders.AwsAuthProvider
import com.africablue.awsapp.translateprovider.AwsTranslateProvider
import com.africablue.awsapp.ui.translate.TranslateChatFragmentViewModel
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider

class TranslateViewModelFactory constructor(
    private val context: Context,
    private val awsCredentialsProvider: AWSCredentialsProvider,
    private val translateProvider: AwsTranslateProvider
): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = with(modelClass) {
        when {
            isAssignableFrom(TranslateChatFragmentViewModel::class.java) ->
                TranslateChatFragmentViewModel(context, awsCredentialsProvider, translateProvider)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}