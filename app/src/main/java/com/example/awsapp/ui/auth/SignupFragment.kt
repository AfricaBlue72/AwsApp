package com.example.awsapp.ui.auth

import android.os.Bundle
import android.util.Log
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
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.awsapp.R
import com.example.awsapp.lifecycle.Application
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.InjectorUtils
import kotlinx.android.synthetic.main.auth_signup.*


class SignupFragment : Fragment() {

    val mLogTag = APP_TAG + this::class.java.simpleName
    val viewModel: SignupViewModel by viewModels{
        InjectorUtils.provideSignupViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_signup, container, false)

        viewModel.authStatus.observe(viewLifecycleOwner, Observer{
            if(it != null && it == AuthStatus.SIGNED_IN_WAIT_FOR_CODE){
                viewModel.navigateToConfirmCode(
                    findNavController(),
                    editUserName.text.toString())
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                root.findViewById<Button>(R.id.buttonSignup).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonSignup).isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        root.findViewById<Button>(R.id.buttonSignup).apply{
            setOnClickListener{
                viewModel.signup(
                    editUserName.text.toString(),
                    editEmail.text.toString(),
                    editPassword.text.toString(),
                    editFirstName.text.toString(),
                    editLastName.text.toString(),
                    editAge.text.toString()
                )
            }
        }
        return root
    }
}