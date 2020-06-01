package com.africablue.awsapp.ui.translate

import com.amazonaws.services.polly.model.Voice

fun Voice.compositeVoiceName(): String{
        var result = this.id

        val engines = getSupportedEngines()

        if(engines.contains("neural")){
            result = result + " | neural"
        }
        else{
            result = result + " | standard"
        }
        return result
}