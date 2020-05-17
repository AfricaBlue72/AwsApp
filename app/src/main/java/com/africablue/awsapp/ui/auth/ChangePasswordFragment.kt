package com.africablue.awsapp.ui.auth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.africablue.awsapp.R
import com.africablue.awsapp.util.APP_TAG
import com.africablue.awsapp.authproviders.ChangePasswordStatus
import com.africablue.awsapp.util.getViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.auth_change_password.*

class ChangePasswordFragment: Fragment() {
    // Use this instance of the interface to deliver action events
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: ChangePasswordViewModel by viewModels{
        getViewModelFactory()
    }

    //private val viewModel by viewModels<AddEditTaskViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.auth_change_password, container, false)

        root.findViewById<TextInputLayout>(R.id.editTextOldPassword).requestFocus()

        viewModel.changePasswordStatus.observe(viewLifecycleOwner, Observer{
            if(it != null && it == ChangePasswordStatus.DONE){
                viewModel.refresh()
                findNavController().popBackStack()
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                root.findViewById<Button>(R.id.buttonChangePassword).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonChangePassword).isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        root.findViewById<Button>(R.id.buttonChangePassword).apply{
            setOnClickListener{
                viewModel.changePassword(
                    editTextOldPassword.editText?.text.toString(),
                    editTextNewPassword.editText?.text.toString())
            }
        }

        return root
    }
}