
package com.example.btmonitork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class DragActivity : AppCompatActivity() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)

        webView= findViewById(R.id.web6)
        //String htmlContent = "<html><body><h1>Hello, world!</h1><p>This is HTML content.</p></body></html>";
        //webView.loadData(htmlContent, "text/html", "UTF-8")
        webView.loadUrl("file:///android_asset/data.html")

    }
}