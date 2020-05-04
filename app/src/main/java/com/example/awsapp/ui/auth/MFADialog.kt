package com.example.awsapp.ui.auth

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus


class MFADialog(forAuthStatus: AuthStatus, listener: VerifyCodeDialogListener) : DialogFragment() {
    internal var listener = listener
    internal val forAuthStatus = forAuthStatus

    interface VerifyCodeDialogListener {
        fun onDialogPositiveClick(forAuthStatus: AuthStatus, mfaCode: String)
        fun onDialogNegativeClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_mfa_dialog, container, false)

        if(forAuthStatus == AuthStatus.NEW_PASSWORD_REQUIRED){
            val header = root.findViewById<TextView>(R.id.textViewHeader)
            header.setText(R.string.auth_new_password)

            val editCode = root.findViewById<EditText>(R.id.editTextCode)
            editCode.setHint(R.string.auth_verification_code_hint)
        }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.auth_mfa_dialog, null)

            if(forAuthStatus == AuthStatus.NEW_PASSWORD_REQUIRED){
                val header = view.findViewById<TextView>(R.id.textViewHeader)
                header.setText(R.string.auth_new_password)

                val editCode = view.findViewById<EditText>(R.id.editTextCode)
                editCode.setHint(R.string.auth_new_password_hint)
                editCode.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                editCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            val submitButton = view.findViewById<Button>(R.id.buttonSubmit)
            submitButton.setOnClickListener{
                val code = view.findViewById<EditText>(R.id.editTextCode).text.toString()
                listener.onDialogPositiveClick(forAuthStatus, code)
                dismiss()
            }

            val cancelButton = view.findViewById<Button>(R.id.buttonCancel)
            cancelButton.setOnClickListener{
                listener.onDialogNegativeClick()
                dismiss()
            }

            val editCode = view.findViewById<EditText>(R.id.editTextCode)
            editCode.requestFocus()

            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}