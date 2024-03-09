package com.example.btmonitork

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class UniverActivity : AppCompatActivity() {
    lateinit var webView: WebView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_univer)
        webView= findViewById(R.id.web6)
        //String htmlContent = "<html><body><h1>Hello, world!</h1><p>This is HTML content.</p></body></html>";
        //webView.loadData(htmlContent, "text/html", "UTF-8")
        webView.loadUrl("file:///android_asset/index.html")

    }
}