package com.africablue.awsapp.translateprovider

import android.util.Log
import com.africablue.awsapp.authproviders.AwsAuthProvider
import com.africablue.awsapp.util.APP_TAG
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.translate.AmazonTranslateClient
import com.amazonaws.services.translate.model.TranslateTextRequest
import com.amazonaws.services.translate.model.TranslateTextResult

object AwsTranslateProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName
    lateinit var translateClient: AmazonTranslateClient

    fun initialize(awsCredentials: AWSCredentials){
        translateClient = AmazonTranslateClient(awsCredentials)
    }

    fun translateText(sourceLanguage: String, targetLanguage: String, text: String): TranslateTextResult?{
        var result: TranslateTextResult? = null
        val request = TranslateTextRequest()

        request.sourceLanguageCode = sourceLanguage
        request.targetLanguageCode = targetLanguage
        request.text = text

        try {
            result = translateClient.translateText(request)
        }
        catch(e: Exception){
            Log.w(mLogTag, "Error: " + e.message)
        }

        return result
    }

}