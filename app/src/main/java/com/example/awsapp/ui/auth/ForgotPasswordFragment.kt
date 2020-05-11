package com.example.awsapp.ui.auth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.awsapp.R
import com.example.awsapp.authproviders.AuthInjectorUtils
import com.example.awsapp.authproviders.AuthStatus
import com.example.awsapp.authproviders.ForgotPasswordStatus
import com.example.awsapp.util.APP_TAG
import kotlinx.android.synthetic.main.auth_forgot_password.*

class ForgotPasswordFragment: Fragment(), FlowDialog.VerifyCodeDialogListener {
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: ForgotPasswordViewModel by viewModels{
        AuthInjectorUtils.provideForgotPasswordViewModelFactory(requireContext())
    }
    var userName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_forgot_password, container, false)

        val buttonSubmit = root.findViewById<Button>(R.id.buttonSubmit)
        val editUser = root.findViewById<EditText>(R.id.editTextUser)

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                buttonSubmit.isEnabled = false
                editUser.isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                buttonSubmit.isEnabled = true
                editUser.isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        viewModel.forgotPasswordStatus.observe(viewLifecycleOwner, Observer {
            if(it != null){
                when(it){
                   ForgotPasswordStatus.CONFIRM -> {
                       val dialog =
                           FlowDialog(AuthStatus.FORGOT_PASSWORD_CODE,
                               this@ForgotPasswordFragment,
                               editUser.text.toString())
                       dialog.show(childFragmentManager, null)
                   }
                    ForgotPasswordStatus.DONE -> {
                        val navController = findNavController()
                        navController.popBackStack()
                    }
                }
            }
        })

        buttonSubmit.setOnClickListener {
            userName = editTextUser.text.toString()
            if(userName != null) {
                viewModel.forgotPassword(userName!!)
            }
        }
        return root
    }

    override fun onDialogPositiveClick(forAuthStatus: AuthStatus, code: String, password:String) {
        if(userName != null) {
            viewModel.confirmForgotPassword(password, code)
        }
    }

    override fun onDialogNegativeClick() {
        val toast = Toast.makeText(getActivity()?.getApplicationContext(), "Rather Not", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 8, 8)
        toast.show()
    }
}