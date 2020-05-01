package com.example.awsapp.ui.auth

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
import com.example.awsapp.R
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import kotlinx.android.synthetic.main.auth_signin.*

class SigninFragment : Fragment(), MFADialog.VerifyCodeDialogListener {
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: SigninViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_signin, container, false)


        viewModel.authStatus.observe(viewLifecycleOwner, Observer{
            if(it != null && it == AuthStatus.SIGNED_IN){
                val navController = findNavController()
                navController.popBackStack()
//                val newFragment = MFADialog(this)
//                newFragment.show(childFragmentManager, null)
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                root.findViewById<Button>(R.id.buttonSignin).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonSignin).isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        root.findViewById<Button>(R.id.buttonSignin).apply{
            setOnClickListener {
                viewModel.signin(
                    editUsername.text.toString(),
                    editPassword.text.toString()
                )
            }

        }

        root.findViewById<Button>(R.id.buttonSignup).apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
            }
        }

        return root
    }

    override fun onDialogPositiveClick(mfaCode: String) {
        val toast = Toast.makeText(getActivity()?.getApplicationContext(), mfaCode, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 8, 8)
        toast.show()
    }

    override fun onDialogNegativeClick() {
        val toast = Toast.makeText(getActivity()?.getApplicationContext(), "Rather Not", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 8, 8)
        toast.show()
    }


}