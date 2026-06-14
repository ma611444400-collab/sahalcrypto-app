package com.example

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var webViewInstance by remember { mutableStateOf<WebView?>(null) }

            // Handle Android back button press inside the nested webview
            BackHandler(enabled = webViewInstance?.canGoBack() == true) {
                webViewInstance?.goBack()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E11)) // Deep Charcoal background to blend cleanly
                    .systemBarsPadding()
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                    // Let local and resource files load internally
                                    if (url != null) {
                                        if (url.startsWith("file://") || url.contains("sahalcrypt.com") || url.contains("github.com") || url.contains("cloudflare") || url.contains("googleapis") || url.contains("gstatic")) {
                                            view?.loadUrl(url)
                                            return true
                                        }
                                    }
                                    return false
                                }
                            }
                            webChromeClient = WebChromeClient()

                            // Set premium features for our browser applet
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                allowFileAccess = true
                                allowContentAccess = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                cacheMode = WebSettings.LOAD_DEFAULT
                                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            }

                            loadUrl("file:///android_asset/login.html")
                            webViewInstance = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
