package com.africablue.awsapp.ui.translate

import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.R
import com.africablue.awsapp.translateprovider.AwsTranslateProvider
import com.africablue.awsapp.util.APP_TAG
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.polly.model.Voice
import com.amazonaws.services.translate.model.TranslateTextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslateChatFragmentViewModel(private val context: Context,
                                     private val awsCredentialsProvider: AWSCredentialsProvider,
                                     public val translateProvider: AwsTranslateProvider): ViewModel(){


    val mLogTag = APP_TAG + this::class.java.simpleName
    val translateDataAdapter = TranslateDataAdapter(context)
    var isTranslating = MutableLiveData<Boolean>().apply{
        value = false
    }

    val languageCodeMap = this.loadLanguageMap()

    var voicesMap = HashMap<String, MutableList<Voice>>()

    var sourceLanguage = MutableLiveData<String>().apply{
        value = "English"
    }
    var targetLanguage = MutableLiveData<String>().apply{
        value = "Dutch"
    }


    init{
        //val countries: TypedArray = context.resources.obtainTypedArray(R.array.countries_array_of_arrays)
        translateProvider.voices.observeForever {
            it.forEach()
            {
                if(voicesMap[it.languageCode] != null){
                    voicesMap[it.languageCode]?.add(it)
                }
                else {
                    voicesMap.put(it.languageCode, mutableListOf(it))
                }
            }
        }
        AwsTranslateProvider.initialize(awsCredentialsProvider)
    }

    fun translate(text: String){
        Log.i(mLogTag, "Size of voice map: " + voicesMap.size)
        Log.i(mLogTag, "Size of voices: " + translateProvider.voices.value?.size)
        isTranslating.value = true
        val sourceCode = this.languageCodeMap?.get(this.sourceLanguage.value)
        val targetCode = this.languageCodeMap?.get(this.targetLanguage.value)


        //Add the source message to the channel
        val message = TranslateChatMessage(
            TranslateChatMessageType.SEND,
            sourceCode?: "auto",
            text)
        TranslateChatData.chatList.add(message)
        translateDataAdapter.notifyItemInserted( (TranslateChatData.chatList.size - 1) )

        //Translate
        viewModelScope.launch {
            _translate(sourceCode?: "en", targetCode?: "nl" , text)
        }
    }

    private suspend fun _translate(sourceLanguage: String, targetLanguage: String, text: String) = withContext(
        Dispatchers.IO){

        val result = translateProvider.translateText(sourceLanguage, targetLanguage, text)

        if(result != null) {
            val message = TranslateChatMessage(
                TranslateChatMessageType.RECEIVE,
                result?.targetLanguageCode,
                result?.translatedText
            )
            TranslateChatData.chatList.add(message)
            isTranslating.postValue(false)
        }
    }


    fun loadLanguageMap(): HashMap<String, String>? {
        val languageMap = context.resources.obtainTypedArray(R.array.countries_array_of_arrays)

        val size = languageMap.length()
        val resultMap  = HashMap<String, String>()

        var countryTypedArray: TypedArray? = null
        for(i in 0 until size){
            val id: Int = languageMap.getResourceId(i, -1)
            if(id == -1){
                Log.e(mLogTag, "Could not read language map from resource file")
                break
            }
            countryTypedArray = context.resources.obtainTypedArray(id)
            val code = countryTypedArray.getString(0)
            val lang = countryTypedArray.getString(1)

            if(code != null && lang != null) {
                resultMap.put(lang, code)
            }
        }
        countryTypedArray?.recycle()
        languageMap.recycle()

        Log.i(mLogTag, "Returning language map with size: " + resultMap.size)

        return resultMap
    }
}