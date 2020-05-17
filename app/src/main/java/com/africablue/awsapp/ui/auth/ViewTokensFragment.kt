package com.africablue.awsapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amazonaws.mobile.client.results.Token
import com.amazonaws.mobile.client.results.Tokens
import com.africablue.awsapp.R
import com.africablue.awsapp.authproviders.AuthInjectorUtils
import com.africablue.awsapp.util.APP_TAG
import java.util.*


class ViewTokensFragment : Fragment() {
    // Use this instance of the interface to deliver action events
    val mLogTag = APP_TAG + this::class.java.simpleName
    private val viewModel: ViewTokensViewModel by viewModels{
        AuthInjectorUtils.provideViewTokensViewModelFactory(requireContext())
    }
    private var currentTokens: Tokens? = null

    @ExperimentalStdlibApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_view_tokens, container, false)

        viewModel.feedback.observe(viewLifecycleOwner, Observer {
            val toast = Toast.makeText(getActivity()?.getApplicationContext(), it, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 8, 8)
            toast.show()
        })

        viewModel.getTokens()

        viewModel.tokens.observe(viewLifecycleOwner, Observer {
            currentTokens = it
            FillContent(root, it?.idToken)
        })

        val spinner: Spinner = root.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.auth_token_types,
            android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                when (parent.getItemAtPosition(position)){
                    getString(R.string.auth_token_openid) -> FillContent(root, currentTokens?.idToken)
                    getString(R.string.auth_token_access) -> FillContent(root, currentTokens?.accessToken)
                    getString(R.string.auth_token_refresh) -> FillContent(root, currentTokens?.refreshToken)

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        //spinner.onItemSelectedListener = this

        return root
    }

    @ExperimentalStdlibApi
    private fun FillContent(root: View, token: Token?) {
        try {
            if (token != null) {
                val expiry = root.findViewById<TextView>(R.id.textViewExpiryContent)
                val issueDate = root.findViewById<TextView>(R.id.textViewIssuedContent)
                val issuedAt = root.findViewById<TextView>(R.id.textViewIssuedAtContent)
                val header = root.findViewById<TextView>(R.id.textViewTokenHeaderContent)
                val body = root.findViewById<TextView>(R.id.textViewTokenBodyContent)

                expiry.text = token?.expiration?.toString()
                issueDate.text = token?.issuedAt?.toString()
                issuedAt.text = token?.issuedAt?.toString()

                val tokenParts = token?.tokenString?.split(".")?.toTypedArray()

                if (tokenParts != null) {
                    header.text = Base64.getDecoder().decode(tokenParts?.get(0)).decodeToString()
                    body.text = Base64.getDecoder().decode(tokenParts?.get(1)).decodeToString()
                }
            }
        }
        catch(e: Exception){
            Log.e(mLogTag, "Error: " + e.message)
        }
    }
}