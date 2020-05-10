package com.example.awsapp.ui.auth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.awsapp.R
import com.example.awsapp.authproviders.AuthStatus
import com.example.awsapp.authproviders.AuthInjectorUtils


class FlowDialog(forAuthStatus: AuthStatus, listener: VerifyCodeDialogListener) : DialogFragment() {
    internal var listener = listener
    internal val forAuthStatus = forAuthStatus

    private val viewModel: FlowDialogViewModel by viewModels{
        AuthInjectorUtils.provideFlowDialogViewModelFactory(requireContext())
    }

    interface VerifyCodeDialogListener {
        fun onDialogPositiveClick(forAuthStatus: AuthStatus, mfaCode: String)
        fun onDialogNegativeClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_flow_dialog, container, false)

        if(forAuthStatus == AuthStatus.NEW_PASSWORD_REQUIRED){
            val header = root.findViewById<TextView>(R.id.textViewHeader)
            header.setText(R.string.auth_new_password)

            val editCode = root.findViewById<EditText>(R.id.editTextCode)
            editCode.setHint(R.string.auth_verification_code_hint)
        }


        val buttonUser = root.findViewById<Button>(R.id.buttonUser)
        val editTextUser = root.findViewById<EditText>(R.id.editTextUser)
        buttonUser.text = viewModel.userName.value
        editTextUser.setText(viewModel.userName.value)
        buttonUser.setOnClickListener(){
            editTextUser.isVisible = true
            buttonUser.isVisible = false
        }

        return root
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
                }
                AuthStatus.SIGNED_UP_WAIT_FOR_CODE -> {
                    val resendButton = view.findViewById<Button>(R.id.buttonResendCode)
                    resendButton.isVisible = true
                }
                else ->{
                    hideResendButton(view)
                }
            }

            val buttonUser = view.findViewById<Button>(R.id.buttonUser)
            val editTextUser = view.findViewById<EditText>(R.id.editTextUser)
            buttonUser.text = viewModel.userName.value
            editTextUser.setText(viewModel.userName.value)
            buttonUser.setOnClickListener(){
                editTextUser.visibility = View.VISIBLE
                buttonUser.visibility = View.INVISIBLE
            }

            val resendButton = view.findViewById<Button>(R.id.buttonResendCode)
            resendButton.setOnClickListener {
                viewModel.resendSignup(editTextUser.text.toString())
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

            viewModel.isBusy.observe(viewLifecycleOwner, Observer {
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

        val editCode = view.findViewById<EditText>(R.id.editTextCode)
        editCode.setHint(R.string.auth_new_password_hint)
        editCode.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        editCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private fun hideResendButton(view: View) {
        val resendButton = view.findViewById<Button>(R.id.buttonResendCode)
        resendButton.isVisible = true
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.frameLayoutForFlow)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.divider,
            ConstraintSet.TOP,
            R.id.editTextCode,
            ConstraintSet.BOTTOM,
            8
        )
        constraintSet.applyTo(constraintLayout)
    }
}