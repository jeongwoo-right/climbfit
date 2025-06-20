package com.scsa.myproject;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);  // JS 필요 시
        webView.setWebViewClient(new WebViewClient());  // 외부 브라우저로 안 넘어가게

        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
