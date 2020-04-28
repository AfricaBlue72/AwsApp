package com.example.awsapp.ui.auth

import android.os.Bundle
import android.view.*
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

class SignoutFragment : Fragment() {
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: SignoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_signout, container, false)

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        viewModel.authStatus.observe(viewLifecycleOwner, Observer{
            if(it != null && it == AuthStatus.SIGNED_OUT){
                val navController = findNavController()
                navController.popBackStack()
            }
        })

        viewModel.isBusy.observe(viewLifecycleOwner, Observer {
            if(it != null && it == true) {
                root.findViewById<Button>(R.id.buttonSignout).isEnabled = false
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if(it != null && it == false) {
                root.findViewById<Button>(R.id.buttonSignout).isEnabled = true
                root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        })

        root.findViewById<Button>(R.id.buttonSignout).apply{
            setOnClickListener {
                viewModel.simpleSignout(false, false)
            }
        }

        return root
    }
}