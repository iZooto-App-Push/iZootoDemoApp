package com.k.deeplinkingtesting

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OutbrainWidgetJSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outbrain_widget_jsactivity)
        val webView: WebView = findViewById(R.id.webView)

//        val htmlContent = """
//    <html>
//        <head>
//            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
//        </head>
//        <body>
//            <div class="OUTBRAIN"
//                data-src="https://www.izooto.com"
//                data-widget-id="App_4"
//                data-ob-user-id="278559ce-b132-4b7f-9078-e20fe121216d"
//                data-ob-installation-key="DATAB2HQ71I65P5JML02NJDEE"
//                data-ob-app-id="com.k.deeplinkingtesting"
//                data-ob-app-ver="1.0.0">
//            </div>
//            <script type="text/javascript" async="async" src="https://widgets.outbrain.com/outbrain.js"></script>
//        </body>
//    </html>
//""".trimIndent()

      //  webView.settings.javaScriptEnabled = true
      //  webView.loadUrl("https://www.izooto.com")
       // webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
//        val script = """
//    (function() {
//        var div = document.createElement('div');
//        div.className = 'OUTBRAIN';
//        div.setAttribute('data-src', 'https://www.izooto.com');
//        div.setAttribute('data-widget-id', 'App_4');
//        div.setAttribute('data-ob-user-id', '278559ce-b132-4b7f-9078-e20fe121216d');
//        div.setAttribute('data-ob-installation-key', 'DATAB2HQ71I65P5JML02NJDEE');
//        div.setAttribute('data-ob-app-id', 'com.k.deeplinkingtesting');
//        div.setAttribute('data-ob-app-ver', '1.0.0');
//
//        document.body.appendChild(div);
//
//        var script = document.createElement('script');
//        script.type = 'text/javascript';
//        script.async = true;
//        script.src = 'https://widgets.outbrain.com/outbrain.js';
//        document.body.appendChild(script);
//    })();
//""".trimIndent()
//
//// Enable JavaScript in WebView
//        webView.settings.javaScriptEnabled = true
//
//// Load a base URL before injecting JavaScript
//        webView.loadUrl("https://www.izooto.com")
//
//// Inject JavaScript after the page is loaded
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                webView.evaluateJavascript(script, null)
//            }
//        }
// Initialize WebView
       // val webView: WebView = findViewById(R.id.webView)

// Enable JavaScript in WebView
        webView.settings.javaScriptEnabled = true

// Define the HTML content
//        val htmlContent = """
//    <!DOCTYPE html>
//    <html>
//    <head>
//        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
//    </head>
//    <body style="margin:0; padding:0;width:'100%'">
//        <div class="OUTBRAIN"
//            data-ob-bundleUrl="https://play.google.com/store/apps/details?id=com.k.deeplinkingtesting"
//            data-ob-language="en"
//            data-widget-id="APP_5"
//            data-ob-user-id="278559ce-b132-4b7f-9078-e20fe121216d"
//            data-ob-installation-key="DATAB2HQ71I65P5JML02NJDEE">
//        </div>
//        <script type="text/javascript" async="async"
//        src="https://widgets.outbrain.com/outbrain.js"></script>
//    </body>
//    </html>
//""".trimIndent()
//
//// Load the HTML content directly into the WebView
//        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

        val htmlContent = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <style>
            /* Remove default margin and padding */
            body {
                margin: 0;
                padding: 0;
                font-family: Arial, sans-serif;
                word-wrap: break-word;
            }

            /* Remove bullets from lists */
            .OUTBRAIN ul {
                list-style: none; /* Remove bullets */
             
            }

        </style>
    </head>
    <body>
        <!-- Outbrain Widget -->
        <div class="OUTBRAIN"
            data-ob-bundleUrl="https://play.google.com/store/apps/details?id=com.k.deeplinkingtesting"
            data-ob-language="en"
            data-widget-id="APP_4"
            data-ob-user-id="278559ce-b132-4b7f-9078-e20fe121216d"
            data-ob-installation-key="DATAB2HQ71I65P5JML02NJDEE">
        </div>
        <script type="text/javascript" async="async" 
        src="https://widgets.outbrain.com/outbrain.js"></script>
    </body>
    </html>
""".trimIndent()

// Load the HTML content into WebView
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

    }
}