package com.africablue.awsapp.translateprovider

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.africablue.awsapp.authproviders.AwsAuthProvider
import com.africablue.awsapp.util.APP_TAG
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.services.polly.AmazonPollyPresigningClient
import com.amazonaws.services.polly.model.DescribeVoicesRequest
import com.amazonaws.services.polly.model.Voice
import com.amazonaws.services.translate.AmazonTranslateClient
import com.amazonaws.services.translate.model.TranslateTextRequest
import com.amazonaws.services.translate.model.TranslateTextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AwsTranslateProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName
    lateinit var translateClient: AmazonTranslateClient
    lateinit var pollyClient: AmazonPollyPresigningClient
    val voices = MutableLiveData<MutableList<Voice>>().apply {
        value = mutableListOf<Voice>()
    }
    //val voices = mutableListOf<Voice>()

    fun initialize(awsCrecentialsProvider: AWSCredentialsProvider){
        translateClient = AmazonTranslateClient(awsCrecentialsProvider.credentials)
        pollyClient = AmazonPollyPresigningClient(awsCrecentialsProvider)
        GlobalScope.launch {
            getVoices()
        }
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
            Log.w(mLogTag, "Translate error: " + e.message)
        }

        return result
    }

    suspend fun getVoices(){
        withContext(Dispatchers.IO){
            getVoicesRecursive(null)
        }
    }

    private suspend fun getVoicesRecursive(token: String?) {
        var request = DescribeVoicesRequest()

        if(token != null){
            request = request.withNextToken(token)
        }
        try {
            val result = pollyClient.describeVoices(request)

            if(result != null) {
                val list = mutableListOf<Voice>()
                if(voices.value != null){
                    list.addAll(voices.value!!)
                }
                list.addAll(result.voices)
                voices.postValue(list)

                Log.i(mLogTag, "Added " + voices.value?.size + " voices.")

                val nextToken = result.nextToken
                if(nextToken != null){
                    getVoicesRecursive(nextToken)
                }
            }
        }
        catch(e: Exception){
            Log.w(mLogTag, "Get voices error: " + e.message)
        }

        return
    }
}