package com.africablue.awsapp.ui.translate


import android.content.Context
import android.content.res.TypedArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.africablue.awsapp.R
import com.africablue.awsapp.translateprovider.AwsTranslateProvider
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.translate.model.TranslateTextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TranslateChatFragmentViewModel(private val context: Context,
                                     private val awsCredentials: AWSCredentials,
                                     public val translateProvider: AwsTranslateProvider): ViewModel(){

    val translateDataAdapter = TranslateDataAdapter(context)
    var isTranslating = MutableLiveData<Boolean>().apply{
        value = false
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
            _translate(sourceLanguage?: "en", targetLanguage?: "nl" , text)
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
}