package com.africablue.awsapp.ui.slideshow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.africablue.awsapp.R

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val buttonLaunchExample = root.findViewById<Button>(R.id.buttonLaunchExample)
        buttonLaunchExample.setOnClickListener {
            val webpage: Uri = Uri.parse("https://www.hollandcasino.nl/")
            val intent = Intent(Intent.ACTION_VIEW, webpage)

            if (intent.resolveActivity(activity?.packageManager!!) != null) {
                startActivity(intent)
            }
        }




        return root
    }
}
