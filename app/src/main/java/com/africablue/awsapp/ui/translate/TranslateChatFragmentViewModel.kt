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
import com.africablue.awsapp.util.DEFAULT_URL
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.polly.model.Voice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class TranslateChatFragmentViewModel(private val context: Context,
                                     private val awsCredentialsProvider: AWSCredentialsProvider,
                                     public val translateProvider: AwsTranslateProvider): ViewModel(), TranslateDataAdapter.MessageListener{


    val mLogTag = APP_TAG + this::class.java.simpleName
    val translateDataAdapter = TranslateDataAdapter(this, context)

    var isTranslating = MutableLiveData<Boolean>().apply{
        value = false
    }

    val languageMap = this.loadLanguageMap()

    var voicesMap = HashMap<String, MutableList<Voice>>()

    var sourceLanguage = MutableLiveData<String>().apply{
        value = "English"
    }
    var targetLanguage = MutableLiveData<String>().apply{
        value = "Dutch"
    }
    var sourceVoice = MutableLiveData<String>().apply{
        value = "Salli | neural"
    }
    var targetVoice = MutableLiveData<String>().apply{
        value = "Lotte | standard"
    }
    var playBackUrl = MutableLiveData<URL>().apply{
        value = URL(DEFAULT_URL)
    }
    var copiedText = MutableLiveData<String>().apply{
        value = ""
    }

    //fun initialize(){
    init{
        //val countries: TypedArray = context.resources.obtainTypedArray(R.array.countries_array_of_arrays)
        translateProvider.voices.observeForever {
            loadVoiceMap(it)
        }
        AwsTranslateProvider.initialize(awsCredentialsProvider)
    }

    fun translate(text: String, sourceVoice: String?, targetVoice: String?){
        isTranslating.value = true
        val sourceCode = this.languageMap?.get(this.sourceLanguage.value)
        val targetCode = this.languageMap?.get(this.targetLanguage.value)


        //Add the source message to the channel
        val message = TranslateChatMessage(
            TranslateChatMessageType.SEND,
            sourceCode?: "auto",
            sourceVoice,
            text)
        TranslateChatData.chatList.add(message)
        translateDataAdapter.notifyItemInserted( (TranslateChatData.chatList.size - 1) )

        //Translate
        viewModelScope.launch {
            _translate(sourceCode?: "en", targetCode?: "nl" , text, targetVoice)
        }
    }

    private suspend fun _translate(sourceLanguage: String, targetLanguage: String, text: String, targetVoice: String?) = withContext(
        Dispatchers.IO){

        val result = translateProvider.translateText(sourceLanguage, targetLanguage, text)

        if(result != null) {
            val message = TranslateChatMessage(
                TranslateChatMessageType.RECEIVE,
                result?.targetLanguageCode,
                targetVoice,
                result?.translatedText
            )
            TranslateChatData.chatList.add(message)
            isTranslating.postValue(false)
        }
    }


    fun getPresignedSynthesizeSpeechUrl(text: String, compositeVoiceName: String) {
        viewModelScope.launch {
            val engine = extractEngineFromCompositeVoice(compositeVoiceName)
            val voice = extractVoiceFromCompositeVoice(compositeVoiceName)

            if(voice != null) {
                _getPresignedSynthesizeSpeechUrl(text, voice, engine)
            }
        }
    }

    private suspend fun _getPresignedSynthesizeSpeechUrl(text: String, voice: String, engine: String?)  = withContext(
        Dispatchers.IO){

        val url =  translateProvider.getPresignedSynthesizeSpeechUrl(text, voice, engine)

        if(url.toString().isNotEmpty() && url.toString().isNotBlank()) {
            playBackUrl.postValue(url)
        }
    }


    private fun loadLanguageMap(): HashMap<String, String>? {
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

    private fun loadVoiceMap(it: MutableList<Voice>) {
        voicesMap.clear()
        it.forEach()
        {
            val code = extractTranslationCountryCode(it.languageCode)
            if (voicesMap[code] != null) {
                voicesMap[code]?.add(it)
            } else {
                voicesMap.put(code, mutableListOf(it))
            }
        }
    }

    override fun onMessageClick(messageText: String?) {
        copiedText.postValue(messageText)
    }

    //Map AWS Polly language codes to AWS Translate language codes
    //Polly: https://docs.aws.amazon.com/polly/latest/dg/SupportedLanguage.html
    //Translate: https://docs.aws.amazon.com/translate/latest/dg/what-is.html
    private fun extractTranslationCountryCode(pollyCode: String): String{
        var translateCode = String()
        val at = pollyCode.indexOf('-')

        if(at >= 0){
            translateCode = pollyCode.substring(0, at)
        }
        else{
            translateCode = pollyCode
        }

        //Moving ISO 639-3 to ISO 639-1
        when (translateCode){
            "cmn" -> { translateCode = "zh"}
            "nb" -> { translateCode = "no"}
            "arb" -> { translateCode = "ar"}
        }

        return translateCode
    }

    private fun extractEngineFromCompositeVoice(compositeVoiceName: String):String? {
        val divider = compositeVoiceName.indexOf(" | ")
        return if (divider < 0)
            null
        else
            compositeVoiceName.substring(divider + 3)
    }

    private fun extractVoiceFromCompositeVoice(compositeVoiceName: String):String? {
        val divider = compositeVoiceName.indexOf(" | ")
        return if (divider < 0)
            compositeVoiceName
        else compositeVoiceName.substring(0, divider)
    }
}