package com.africablue.awsapp.ui.translate

import android.app.Dialog
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.africablue.awsapp.R
import com.google.android.material.textfield.TextInputLayout


class TranslateLanguageDialog(private val listener: LanguageDialogListener, private val languages: List<String>)  : DialogFragment() {

    interface LanguageDialogListener {
        fun onDialogPositiveClick(sourceLanguage: String?, targetLanguage: String?)
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
                val targetLang = view.findViewById<TextInputLayout>(R.id.textFieldTargetLanguage).editText?.text.toString()
                listener.onDialogPositiveClick(sourceLang, targetLang)
                dismiss()
            }

            val cancelButton = view.findViewById<Button>(R.id.buttonCancel)
            cancelButton.setOnClickListener{
                listener.onDialogNegativeClick()
                dismiss()
            }

            val sourceLanguage = view.findViewById<TextInputLayout>(R.id.textFieldSourceLanguage)
            val sourceAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
            (sourceLanguage.editText as? AutoCompleteTextView)?.setAdapter(sourceAdapter)

            val targetLanguage = view.findViewById<TextInputLayout>(R.id.textFieldTargetLanguage)
            val targetAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
            (targetLanguage.editText as? AutoCompleteTextView)?.setAdapter(targetAdapter)

            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false

            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }


}