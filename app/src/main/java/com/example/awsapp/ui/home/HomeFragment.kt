package com.example.awsapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.UserState
import com.example.awsapp.R
import com.example.awsapp.util.APP_TAG

class HomeFragment : Fragment() {
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
//        root.findViewById<Button>(R.id.testSignout).apply {
//            setOnClickListener(){
//                AWSMobileClient.getInstance().signOut()
//            }
//        }
//        CheckAuth()
        return root
    }

    private fun CheckAuth() {
        val awsClient = AWSMobileClient.getInstance()

        val userState = awsClient.currentUserState().userState


        Log.i(mLogTag, "onResult: " + userState)
        when (userState) {
            UserState.SIGNED_IN -> {
                Log.i(mLogTag, "User is signed in")
            }
            UserState.SIGNED_OUT -> {
                Log.i(mLogTag, "User is logged out")
                val navController = findNavController()
                navController.navigate(R.id.nav_sign_in,null)
            }
            else -> {
                awsClient.signOut()
                Log.w(mLogTag, "Status is" + userState)
            }
        }
    }
}
