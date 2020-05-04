package com.example.awsapp.ui.auth

import android.os.Bundle
import android.util.Log
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
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.awsapp.R
import com.example.awsapp.lifecycle.Application
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.InjectorUtils
import kotlinx.android.synthetic.main.auth_signup.*


class SignupFragment : Fragment() , MFADialog.VerifyCodeDialogListener{

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
            when( it ) {
                AuthStatus.SIGNED_UP_WAIT_FOR_CODE -> {
                    val dialog = MFADialog(it, this@SignupFragment)
                    dialog.show(childFragmentManager, null)
                }
                AuthStatus.SIGNED_UP,
                    AuthStatus.SIGNED_IN ->{
                    val navController = findNavController()
                    navController.popBackStack()
                }
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                //TODO refactor as a toggle.
                root.findViewById<Button>(R.id.buttonSignup).isEnabled = false
                root.findViewById<EditText>(R.id.editUserName).isEnabled = false
                root.findViewById<EditText>(R.id.editEmail).isEnabled = false
                root.findViewById<EditText>(R.id.editPassword).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonSignup).isEnabled = true
                root.findViewById<EditText>(R.id.editUserName).isEnabled = true
                root.findViewById<EditText>(R.id.editEmail).isEnabled = true
                root.findViewById<EditText>(R.id.editPassword).isEnabled = true
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
                    editPassword.text.toString()
                )
            }
        }
        return root
    }
    override fun onDialogPositiveClick(forAuthStatus: AuthStatus, mfaCode: String) {
        when(forAuthStatus) {
            AuthStatus.SIGNED_UP_WAIT_FOR_CODE -> {
                viewModel.confirmSignup(mfaCode)
                val toast =
                    Toast.makeText(
                        getActivity()?.getApplicationContext(),
                        mfaCode,
                        Toast.LENGTH_LONG
                    )
                toast.setGravity(Gravity.TOP, 8, 8)
                toast.show()
            }
        }
    }

    override fun onDialogNegativeClick() {
        val navController = findNavController()
        navController.popBackStack()
        val toast = Toast.makeText(getActivity()?.getApplicationContext(), "Rather Not", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 8, 8)
        toast.show()
    }
}