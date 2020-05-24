package com.africablue.awsapp.ui.translate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.africablue.awsapp.R
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class TranslateDataAdapter(
    private val context: Context
) : RecyclerView.Adapter<TranslateDataAdapter.BaseTranslateViewHolder<*>>() {

    companion object {
        const val TYPE_RECEIVE = 0
        const val TYPE_SEND = 1
    }

    override fun getItemViewType(position: Int): Int {
        val messageType = TranslateChatData.chatList[position].messageType
        return when (messageType) {
            TranslateChatMessageType.RECEIVE-> TYPE_RECEIVE
            TranslateChatMessageType.SEND -> TYPE_SEND
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    //--------onCreateViewHolder: inflate layout with view holder-------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTranslateViewHolder<*> {
        return when (viewType) {
            TYPE_RECEIVE -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.translate_message_received_view, parent, false)
                ReceiveMessageViewHolder(view)
            }
            TYPE_SEND -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.translate_message_sent_view, parent, false)
                SendMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    //-----------onCreateViewHolder: bind view with data model---------
    override fun onBindViewHolder(holder: BaseTranslateViewHolder<*>, position: Int) {
        val message = TranslateChatData.chatList[position]
        when (holder) {
            is ReceiveMessageViewHolder -> holder.bind(message)
            is SendMessageViewHolder -> holder.bind(message)
            else -> throw IllegalArgumentException()
        }
    }

    abstract class BaseTranslateViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ReceiveMessageViewHolder(itemView: View) : BaseTranslateViewHolder<TranslateChatMessage>(itemView) {

        override fun bind(item: TranslateChatMessage) {
            val header = itemView.findViewById<TextView>(R.id.textViewReceiveMessageHeader)
            val mainText = itemView.findViewById<TextView>(R.id.textViewReceiveMessageMain)
            val footer = itemView.findViewById<TextView>(R.id.textViewReceiveMessageFooter)

            header.text = item?.languageCode
            mainText.text = item?.text
            val date =
                DateFormat.getDateTimeInstance().format(Date())

            footer.text = date.toString()
        }
    }
    class SendMessageViewHolder(itemView: View) : BaseTranslateViewHolder<TranslateChatMessage>(itemView) {

        override fun bind(item: TranslateChatMessage) {
            val header = itemView.findViewById<TextView>(R.id.textViewSentMessageHeader)
            val mainText = itemView.findViewById<TextView>(R.id.textViewSentMessageMain)
            val footer = itemView.findViewById<TextView>(R.id.textViewSentMessageFooter)

            header.text = item?.languageCode
            mainText.text = item?.text
            val date =
                DateFormat.getDateTimeInstance().format(Date())

            footer.text = date.toString()
        }
    }

    override fun getItemCount(): Int {
        return TranslateChatData.chatList.size
    }
}