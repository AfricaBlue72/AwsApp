package com.africablue.awsauth.ui.auth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.africablue.awsauth.R
import com.africablue.awsauth.authproviders.AuthStatus
import com.africablue.awsauth.authproviders.AuthInjectorUtils
import com.google.android.material.textfield.TextInputLayout


class FlowDialog(var forAuthStatus: AuthStatus,
                 val listener: VerifyCodeDialogListener,
                 val currentUserName: String? = null) : DialogFragment() {
//    internal var listener = listener
//    internal val forAuthStatus = forAuthStatus

    private val viewModel: FlowDialogViewModel by viewModels{
        AuthInjectorUtils.provideFlowDialogViewModelFactory(requireContext())
    }

    interface VerifyCodeDialogListener {
        fun onDialogPositiveClick(forAuthStatus: AuthStatus, code: String, password: String)
        fun onDialogNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.auth_flow_dialog, null)

            when(forAuthStatus) {
                AuthStatus.NEW_PASSWORD_REQUIRED -> {
                    setupForNewPassword(view)
                    hideResendButton(view)
                    hideCodeInput(view)
                }
                AuthStatus.SIGNED_UP_WAIT_FOR_CODE -> {
                    hidePasswordInput(view)
                }
                AuthStatus.FORGOT_PASSWORD_CODE -> {
                    hideResendButton(view)
                }
                else ->{
                    hidePasswordInput(view)
                    hideResendButton(view)
                }
            }

            val buttonUser = view.findViewById<Button>(R.id.buttonUser)
            val editTextUser = view.findViewById<TextInputLayout>(R.id.editTextUser)
            buttonUser.text = currentUserName ?: viewModel.userName.value
            editTextUser.editText?.setText(currentUserName ?: viewModel.userName.value)

            val resendButton = view.findViewById<Button>(R.id.buttonResendCode)
            resendButton.setOnClickListener {
                viewModel.resendSignup(editTextUser.editText?.text.toString())
            }

            val submitButton = view.findViewById<Button>(R.id.buttonSubmit)
            submitButton.setOnClickListener{
                val code = view.findViewById<TextInputLayout>(R.id.editTextCode).editText?.text.toString()
                val password = view.findViewById<TextInputLayout>(R.id.editTextPassword).editText?.text.toString()
                listener.onDialogPositiveClick(forAuthStatus, code, password)
                dismiss()
            }

            val cancelButton = view.findViewById<Button>(R.id.buttonCancel)
            cancelButton.setOnClickListener{
                listener.onDialogNegativeClick()
                dismiss()
            }

            val editCode = view.findViewById<TextInputLayout>(R.id.editTextCode)
            editCode.requestFocus()

            viewModel.isBusy.observe(this, Observer {
                if(it != null && it == true) {
                    submitButton.isEnabled = false
                    resendButton.isEnabled = false
                    buttonUser.isEnabled = false
                    view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                }
                if(it != null && it == false) {
                    submitButton.isEnabled = true
                    resendButton.isEnabled = true
                    buttonUser.isEnabled = true
                    view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
                }
            })

            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupForNewPassword(view: View) {
        val header = view.findViewById<TextView>(R.id.textViewHeader)
        header.setText(R.string.auth_new_password)

        val editPassword = view.findViewById<TextInputLayout>(R.id.editTextPassword)
        editPassword.hint = getString(R.string.auth_new_password_hint)
    }

    private fun hideResendButton(view: View) {
        val resendButton = view.findViewById<Button>(R.id.buttonResendCode)
        resendButton.isVisible = false
        resendButton.isEnabled = false
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.frameLayoutForFlow)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.divider,
            ConstraintSet.TOP,
            R.id.divider3,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun hidePasswordInput(view: View){
        val editTextPassword = view.findViewById<TextInputLayout>(R.id.editTextPassword)
        editTextPassword.isVisible = false
        editTextPassword.isEnabled = false
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.frameLayoutForFlow)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.editTextCode,
            ConstraintSet.TOP,
            R.id.textViewHeader,
            ConstraintSet.BOTTOM,
            8
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun hideCodeInput(view: View){
        val editTextCode = view.findViewById<TextInputLayout>(R.id.editTextCode)
        editTextCode.isVisible = false
        editTextCode.isEnabled = false
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.frameLayoutForFlow)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.divider3,
            ConstraintSet.TOP,
            R.id.editTextPassword,
            ConstraintSet.BOTTOM,
            8
        )
        constraintSet.applyTo(constraintLayout)
    }
}