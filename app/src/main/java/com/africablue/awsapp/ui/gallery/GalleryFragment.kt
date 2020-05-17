package com.africablue.awsapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.africablue.awsapp.R


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val webView: WebView = root.findViewById(R.id.webView)

        galleryViewModel.url.observe(viewLifecycleOwner, Observer {
            webView.loadUrl(it)
        })
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        return root
    }
}
