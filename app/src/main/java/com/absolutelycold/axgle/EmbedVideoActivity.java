package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

public class EmbedVideoActivity extends AppCompatActivity {

    private String embeddedUrl;
    private WebView webView;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embed_video);
        Intent intent = getIntent();
        embeddedUrl = intent.getStringExtra("embeddedUrl");

        String frameVideo = "<iframe width=\"100%\" height=\"100%\" src=\"" + embeddedUrl + "\" frameborder=\"0\" allowfullscreen></iframe>";

        getSupportActionBar().hide();
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        decorView.setBackgroundColor(Color.BLACK);
        webView = findViewById(R.id.embed_video_web_view);
        webView.setBackgroundColor(Color.TRANSPARENT);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadDataWithBaseURL("https://absolutelycold.github.io", frameVideo, "text/html", null, null);
        //webView.loadDataWithBaseURL(null, frameVideo, "text/html", "utf-8", null);
        //webView.loadUrl(embeddedUrl, extraHeader);
    }

    @Override
    protected void onPostResume() {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onPostResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("embedded_url", embeddedUrl);
    }
}
