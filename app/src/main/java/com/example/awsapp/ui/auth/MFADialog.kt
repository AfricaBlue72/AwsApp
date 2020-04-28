package com.example.awsapp.ui.auth

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.awsapp.R

class MFADialog(listener: VerifyCodeDialogListener) : DialogFragment() {
    internal var listener = listener

    interface VerifyCodeDialogListener {
        fun onDialogPositiveClick(mfaCode: String)
        fun onDialogNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.auth_mfa_dialog, null)

            builder.setView(view)
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the host activity
                        val code = view.findViewById<EditText>(R.id.editTextCode).text.toString()
                        listener.onDialogPositiveClick(code)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}