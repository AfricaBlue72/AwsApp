package com.africablue.awsapp.ui.translate

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.africablue.awsapp.R
import com.africablue.awsapp.util.APP_TAG
import com.africablue.awsapp.util.getViewModelFactoryForTranslateChat
import com.google.android.material.textfield.TextInputLayout


class TranslateLanguageDialog(private val listener: LanguageDialogListener)  : DialogFragment() {
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: TranslateChatFragmentViewModel by viewModels{
        getViewModelFactoryForTranslateChat()
    }

    interface LanguageDialogListener {
        fun onDialogPositiveClick(sourceLanguage: String?,
                                  sourceVoice: String?,
                                  targetLanguage: String?,
                                  targetVoice: String?,
                                  autoPlayTarget: Boolean = false)
        fun onDialogNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.translate_language_dialog, null)

            val buttonOk = view.findViewById<Button>(R.id.buttonOK)
            buttonOk.setOnClickListener{
                val sourceLang = view.findViewById<TextInputLayout>(R.id.textFieldSourceLanguage).editText?.text.toString()
                val sourceVoice = view.findViewById<TextInputLayout>(R.id.textFieldSourceVoice).editText?.text.toString()
                val targetLang = view.findViewById<TextInputLayout>(R.id.textFieldTargetLanguage).editText?.text.toString()
                val targetVoice = view.findViewById<TextInputLayout>(R.id.textFieldTargetVoice).editText?.text.toString()
                var targetAutoPlay = view.findViewById<CheckBox>(R.id.checkBoxTargetAutoPlay).isChecked
                listener.onDialogPositiveClick(sourceLang, sourceVoice, targetLang, targetVoice, targetAutoPlay)
                dismiss()
            }

            val cancelButton = view.findViewById<Button>(R.id.buttonCancel)
            cancelButton.setOnClickListener{
                listener.onDialogNegativeClick()
                dismiss()
            }

            val map = viewModel.languageMap
            if(map != null) {
                val list = ArrayList(map.keys).sorted()

                setSourceLanguageLayout(view, list)
                setTargetLanguageLayout(view, list)
            }

            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false

            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setSourceLanguageLayout(
        view: View,
        sourceLanguagelist: List<String>
    ) {
        val sourceLanguage = view.findViewById<TextInputLayout>(R.id.textFieldSourceLanguage)
        val sourceLanguageAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sourceLanguagelist)

        val sourceVoice = view.findViewById<TextInputLayout>(R.id.textFieldSourceVoice)

        (sourceLanguage.editText as? AutoCompleteTextView)?.setAdapter(sourceLanguageAdapter)

        (sourceLanguage.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            setVoiceLayout(parent, position, sourceVoice)
        }
    }

    private fun setTargetLanguageLayout(
        view: View,
        targetLanguagelist: List<String>
    ) {
        val targetLanguage = view.findViewById<TextInputLayout>(R.id.textFieldTargetLanguage)
        val targetLanguageAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, targetLanguagelist)

        (targetLanguage.editText as? AutoCompleteTextView)?.setAdapter(targetLanguageAdapter)

        val targetVoice = view.findViewById<TextInputLayout>(R.id.textFieldTargetVoice)

        (targetLanguage.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            setVoiceLayout(parent, position, targetVoice)
        }
    }

    private fun setVoiceLayout(
        parent: AdapterView<*>,
        position: Int,
        voiceLayout: TextInputLayout
    ) {
        val language = parent.getItemAtPosition(position).toString()

        val languageCode = viewModel.languageMap?.get(language)

        val adapterList = mutableListOf<String>()

        if (languageCode != null && languageCode.isNotEmpty()) {
            val voicesList = viewModel.voicesMap.get(languageCode)
            if (voicesList != null) {
                voicesList.forEach() {
                    adapterList.add(it.compositeVoiceName())
                }
                voiceLayout.isEnabled = true
            }
        } else {
            voiceLayout.isEnabled = false
        }

        val voiceAdapter =
            ArrayAdapter(requireContext(),
                         android.R.layout.simple_spinner_item,
                         adapterList)

        (voiceLayout.editText as? AutoCompleteTextView)?.setAdapter(voiceAdapter)
    }
}