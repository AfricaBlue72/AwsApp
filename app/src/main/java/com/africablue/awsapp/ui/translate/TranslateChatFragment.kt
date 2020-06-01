package com.africablue.awsapp.ui.translate

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.africablue.awsapp.R
import com.africablue.awsapp.util.APP_TAG
import com.africablue.awsapp.util.DEFAULT_URL
import com.africablue.awsapp.util.getViewModelFactoryForTranslateChat
import com.google.android.material.textfield.TextInputLayout
import java.net.URL


class TranslateChatFragment : Fragment(), TranslateLanguageDialog.LanguageDialogListener {
    private val viewModel: TranslateChatFragmentViewModel by viewModels{
        getViewModelFactoryForTranslateChat()
    }

    private val mLogTag = APP_TAG + this::class.java.simpleName
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.translate_chat_fragment, container, false)

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = viewModel.translateDataAdapter

        recyclerView = root.findViewById<RecyclerView>(R.id.chatRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        viewModel.isTranslating.observe(viewLifecycleOwner, Observer {
            if(it != null && it == false) {
                val position = TranslateChatData.chatList.size - 1
                viewModel.translateDataAdapter.notifyItemInserted(position)
                if(position > 1) {
                    recyclerView.smoothScrollToPosition(position)
                }
            }
        })
        val input = root.findViewById<TextInputLayout>(R.id.textViewInput)

        viewModel.sourceLanguage.observe(viewLifecycleOwner, Observer{
            input.hint = viewModel.sourceLanguage.value + " -> " + viewModel.targetLanguage.value
        })

        viewModel.targetLanguage.observe(viewLifecycleOwner, Observer{
            input.hint = viewModel.sourceLanguage.value + " -> " + viewModel.targetLanguage.value
        })

        viewModel.playBackUrl.observe(viewLifecycleOwner, Observer{
            if(!it.toString().equals(DEFAULT_URL)) {
                playText(it)
            }
        })

        input.setEndIconOnClickListener{
            val text = input.editText?.text.toString()
            viewModel.translate(text, viewModel.sourceVoice.value, viewModel.targetVoice.value)
            input.editText?.text?.clear()
        }

        val configure = root.findViewById<TextInputLayout>(R.id.textViewInput)
        configure.setStartIconOnClickListener{
            val map = viewModel.languageMap
            if(map != null) {
                val dialog = TranslateLanguageDialog(this@TranslateChatFragment)
                dialog.show(childFragmentManager, null)
            }
        }
        return root
    }

    fun playText(url: URL){
        try {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            // Set media player's data source to previously obtained URL.
            mediaPlayer.setDataSource(url.toString())
            Log.i(mLogTag, "Using URL " + url.toString())
            // Prepare the MediaPlayer asynchronously (since the data source is a network stream).
            mediaPlayer.prepareAsync()

            // Set the callback to start the MediaPlayer when it's prepared.
            mediaPlayer.setOnPreparedListener { mp -> mp.start() }

            // Set the callback to release the MediaPlayer after playback is completed.
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        } catch (e: Exception) {
            Log.e(mLogTag, "Unable to set data source for the media player! " + e.message)
        }
    }

    override fun onDialogPositiveClick(
        sourceLanguage: String?,
        sourceVoice: String?,
        targetLanguage: String?,
        targetVoice: String?,
        autoPlayTarget: Boolean
    ) {
        if (sourceLanguage != null && sourceLanguage.isNotEmpty()) {
                viewModel.sourceLanguage.value = sourceLanguage
        }
        if (targetLanguage != null && targetLanguage.isNotEmpty()) {
            viewModel.targetLanguage.value = targetLanguage
        }
        if (sourceVoice != null && sourceVoice.isNotEmpty()){
            viewModel.sourceVoice.value = sourceVoice
        }
        if (targetVoice != null && targetVoice.isNotEmpty()){
            viewModel.targetVoice.value = targetVoice
        }
    }

    override fun onDialogNegativeClick() {

    }

}