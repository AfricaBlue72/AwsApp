package com.example.awsapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.UserState
import com.example.awsapp.R
import com.example.awsapp.ui.auth.MFADialog
import com.example.awsapp.util.APP_TAG

class HomeFragment : Fragment(), MFADialog.VerifyCodeDialogListener{
    val mLogTag = APP_TAG + this::class.java.simpleName

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        root.findViewById<Button>(R.id.testSignout).apply {
            setOnClickListener(){
                val dialog = MFADialog(this@HomeFragment)

                dialog.show(childFragmentManager, null)
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
