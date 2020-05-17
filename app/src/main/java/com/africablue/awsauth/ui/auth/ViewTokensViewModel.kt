package com.africablue.awsauth.ui.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.results.Tokens
import com.africablue.awsauth.R
import com.africablue.awsauth.authproviders.BaseAuthProvider
import com.africablue.awsauth.util.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewTokensViewModel (context: Context, authProvider: BaseAuthProvider)
    : BaseAuthViewModel(context,authProvider){
    private val mLogTag = APP_TAG + this::class.java.simpleName

    val tokens = MutableLiveData<Tokens?>(null)

    fun getTokens() {
        isBusy.value = true

        viewModelScope.launch {
            _getTokens()
            isBusy.postValue(false)
        }
    }

    private suspend fun _getTokens() = withContext(
        Dispatchers.IO){

        val result = authProvider.getTokens()

        if(result == null){
            feedback.postValue( context.getString(R.string.auth_message_get_tokens_nok) )
        }
        else{
            tokens.postValue(result)
            feedback.postValue( context.getString(R.string.auth_message_get_tokens_ok) )
        }

    }
}