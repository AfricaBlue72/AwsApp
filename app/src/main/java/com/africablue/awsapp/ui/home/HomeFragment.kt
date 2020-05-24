package com.africablue.awsapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.africablue.awsapp.R
import com.africablue.awsapp.ui.translate.TranslateChatFragment
import com.africablue.awsapp.util.APP_TAG
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment(){
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

        val card = root.findViewById<MaterialCardView>(R.id.translateCard)

        card.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_translateChatFragment)
        }

        return root
    }
}
//https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-type
//https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
//https://medium.com/@ivancse.58/android-and-kotlin-recyclerview-with-multiple-view-types-65285a254393
//https://medium.com/@paulnunezm/working-with-recyclerview-and-multiple-view-types-bb1e7dfc6993
//https://stackoverflow.com/questions/3013655/creating-hashmap-map-from-xml-resources/40137782#40137782