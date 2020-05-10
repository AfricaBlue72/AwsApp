package com.example.awsapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.awsapp.R
import com.example.awsapp.authproviders.AuthInjectorUtils
import com.example.awsapp.util.APP_TAG

class ForgotPasswordFragment: Fragment() {
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: ConfirmCodeViewModel by viewModels{
        AuthInjectorUtils.provideConfirmCodeViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_confirm_code, container, false)


        return root
    }
}