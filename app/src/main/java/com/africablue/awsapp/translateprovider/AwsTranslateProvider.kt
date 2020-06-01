package com.africablue.awsapp.translateprovider

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.africablue.awsapp.util.APP_TAG
import com.africablue.awsapp.util.DEFAULT_URL
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.polly.AmazonPollyPresigningClient
import com.amazonaws.services.polly.model.*
import com.amazonaws.services.translate.AmazonTranslateClient
import com.amazonaws.services.translate.model.TranslateTextRequest
import com.amazonaws.services.translate.model.TranslateTextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


object AwsTranslateProvider {
    val mLogTag = APP_TAG + this::class.java.simpleName
    lateinit var translateClient: AmazonTranslateClient
    lateinit var pollyClient: AmazonPollyPresigningClient
    private val _voices = mutableListOf<Voice>()

    val voices  = MutableLiveData<MutableList<Voice>>().apply {
        //value = mutableListOf<Voice>()
        value = _voices
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

    fun getPresignedSynthesizeSpeechUrl(text: String, voice: String, engine: String?): URL
    {
        var presignedSynthesizeSpeechUrl: URL = URL(DEFAULT_URL)
        try {
            // Create speech synthesis request.
            val synthesizeSpeechPresignRequest: SynthesizeSpeechPresignRequest =
                SynthesizeSpeechPresignRequest()
                    .withText(text)
                    .withVoiceId(voice)
                    .withOutputFormat(OutputFormat.Mp3)

            if(engine != null && engine?.equals("neural"))
                synthesizeSpeechPresignRequest.withEngine(Engine.Neural)

            // Get the presigned URL for synthesized speech audio stream.
            presignedSynthesizeSpeechUrl =
                pollyClient.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest)
        }
        catch(e: Exception){
            Log.w(mLogTag, "Get voices error: " + e.message)
        }
        return presignedSynthesizeSpeechUrl
    }

    suspend fun getVoices(){
        //Only load once
        if(_voices.isEmpty()) {
            withContext(Dispatchers.IO) {
                _voices.addAll(getVoicesRecursive(null))
                voices.postValue(_voices)
            }
        }
    }

    private suspend fun getVoicesRecursive(token: String?): MutableList<Voice> {
        var request = DescribeVoicesRequest()
        var result = mutableListOf<Voice>()

        if(token != null){
            request = request.withNextToken(token)
        }
        try {
            val describeVoicesResult = pollyClient.describeVoices(request)

            if(describeVoicesResult != null) {
                result.addAll(describeVoicesResult.voices)

                Log.i(mLogTag, "Added " + result.size + " voices.")

                val nextToken = describeVoicesResult.nextToken
                if(nextToken != null){
                    result.addAll( getVoicesRecursive(nextToken) )
                }
            }
        }
        catch(e: Exception){
            Log.w(mLogTag, "Get voices error: " + e.message)
        }
        return result
    }
}