package com.africablue.awsapp.ui.translate


import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.R
import com.africablue.awsapp.translateprovider.AwsTranslateProvider
import com.africablue.awsapp.util.APP_TAG
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.translate.model.TranslateTextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TranslateChatFragmentViewModel(private val context: Context,
                                     private val awsCredentials: AWSCredentials,
                                     public val translateProvider: AwsTranslateProvider): ViewModel(){


    val mLogTag = APP_TAG + this::class.java.simpleName
    val translateDataAdapter = TranslateDataAdapter(context)
    var isTranslating = MutableLiveData<Boolean>().apply{
        value = false
    }
    val languageCodeMap = this.loadLanguageMap()
    var sourceLanguageCode = MutableLiveData<String>().apply{
        value = "en"
    }
    var targetLanguageCode = MutableLiveData<String>().apply{
        value = "nl"
    }

    init{
        val countries: TypedArray = context.resources.obtainTypedArray(R.array.countries_array_of_arrays)
        AwsTranslateProvider.initialize(awsCredentials)
    }

    fun translate(sourceLanguage: String?, targetLanguage: String?, text: String){
        isTranslating.value = true

        val message = TranslateChatMessage(
            TranslateChatMessageType.SEND,
            sourceLanguage?: "auto",
            text)
        TranslateChatData.chatList.add(message)
        translateDataAdapter.notifyItemInserted( (TranslateChatData.chatList.size - 1) )

        viewModelScope.launch {
            _translate(sourceLanguage?: "en", targetLanguage?: "fr" , text)
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