package com.africablue.awsapp.ui.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.africablue.awsapp.R
import com.africablue.awsapp.util.getViewModelFactoryForTranslateChat
import com.google.android.material.textfield.TextInputLayout

class TranslateChatFragment : Fragment(), TranslateLanguageDialog.LanguageDialogListener {
    private val viewModel: TranslateChatFragmentViewModel by viewModels{
        getViewModelFactoryForTranslateChat()
    }

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

        input.setEndIconOnClickListener{
            val text = input.editText?.text.toString()
            viewModel.translate(text)
            input.editText?.text?.clear()
        }

        val configure = root.findViewById<TextInputLayout>(R.id.textViewInput)
        configure.setStartIconOnClickListener{
            val map = viewModel.languageCodeMap
            if(map != null) {
                val dialog = TranslateLanguageDialog(this@TranslateChatFragment)
                dialog.show(childFragmentManager, null)
            }
        }
        return root
    }

    override fun onDialogPositiveClick(sourceLanguage: String?, targetLanguage: String?) {
        if (sourceLanguage != null) {
                viewModel.sourceLanguage.value = sourceLanguage
        }
        if (targetLanguage != null) {
            viewModel.targetLanguage.value = targetLanguage
        }
    }

    override fun onDialogNegativeClick() {

    }

}