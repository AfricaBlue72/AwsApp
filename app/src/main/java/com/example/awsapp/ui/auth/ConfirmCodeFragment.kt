package com.example.awsapp.ui.auth

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.awsapp.R
import com.example.awsapp.lifecycle.Application
import com.example.awsapp.providers.AuthStatus
import com.example.awsapp.util.APP_TAG
import com.example.awsapp.util.InjectorUtils
import kotlinx.android.synthetic.main.auth_confirm_code.*
import kotlinx.android.synthetic.main.auth_signup.*

class ConfirmCodeFragment: Fragment() {
    // Use this instance of the interface to deliver action events
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: ConfirmCodeViewModel by viewModels{
        InjectorUtils.provideConfirmCodeViewModelFactory(requireContext())
    }
    private val args: ConfirmCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.auth_confirm_code, container, false)

        val editTextUserNameForCode = root.findViewById<EditText>(R.id.editTextUserName)
        editTextUserNameForCode.setText(args.userName)

        viewModel.authStatus.observe(viewLifecycleOwner, Observer{
            if(it != null && it == AuthStatus.SIGNED_UP){
                findNavController().popBackStack(R.id.nav_sign_in, true)
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                root.findViewById<Button>(R.id.buttonConfirmCodeOk).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonConfirmCodeOk).isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        root.findViewById<Button>(R.id.buttonConfirmCodeOk).apply{
            setOnClickListener{
                viewModel.confirmSignup(
                    editTextUserName.text.toString(),
                    editTextVerificationCode.text.toString())
            }
        }

        return root
    }
}