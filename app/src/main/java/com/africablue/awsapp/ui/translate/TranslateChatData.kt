package com.africablue.awsapp.ui.translate

enum class TranslateChatMessageType(value: Int){
    RECEIVE (0),
    SEND (1)
}

data class TranslateChatMessage(val messageType: TranslateChatMessageType,
                                val languageCode: String?,
                                val text: String?) {
}

object TranslateChatData{
    val chatList = ArrayList<TranslateChatMessage>()

}