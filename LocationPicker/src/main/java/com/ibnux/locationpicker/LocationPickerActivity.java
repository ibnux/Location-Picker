package com.ibnux.locationpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationPickerActivity extends AppCompatActivity {
    String mPerms[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    WebView webView;
    ProgressBar progressBar;
    
    String mapsUrl = "https://www.google.com/maps/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        webView.setWebChromeClient(chrome);
        webView.setWebViewClient(webViewClient);
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationDatabasePath( getFilesDir().getPath() );
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36");
        settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(mPerms, 3312);
                return;
            }else{
                webView.loadUrl(mapsUrl);
            }
        }else{
            webView.loadUrl(mapsUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==3312){
            //granted or not, still load maps, but user can't select my location
            webView.loadUrl(mapsUrl);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(!url.startsWith(mapsUrl)){
                view.stopLoading();
                view.goBack();
                return;
            }
            log("onPageStarted  "+url);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            log("onPageFinished  "+url);
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            //log("onLoadResource "+url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            progressBar.setVisibility(View.GONE);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            progressBar.setVisibility(View.GONE);
            super.onReceivedHttpError(view, request, errorResponse);
        }

    };

    WebChromeClient chrome = new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(progressBar.isIndeterminate()) {
                progressBar.setIndeterminate(false);
                progressBar.setMax(100);
            }
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin,true,true);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };

    public void log(String txt){
        Log.d("Mpicker",txt);

    }

    public void chooseLocation(View v){
        String url = webView.getUrl();
        log(url);
        // try to use regex
        Pattern p = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        Matcher m = p.matcher(webView.getUrl());
        if(m.find()) {
            log(m.group(0));
            String[] koordinat = m.group(0).split(",");
            if (koordinat.length > 1) {
                log(koordinat[0] + "," + koordinat[1]);
                sendResult(koordinat[0],koordinat[1]);
            }
        }else {
            // using Split
            String[] koordinat = url.split("@")[1].split(",");
            if (koordinat.length > 1) {
                log(koordinat[0] + "," + koordinat[1]);
                sendResult(koordinat[0],koordinat[1]);
            }
        }
    }

    private void sendResult(String lat, String lon){
        Intent i = getIntent();
        i.putExtra("lat", lat);
        i.putExtra("lon", lon);
        setResult(RESULT_OK, i);
        finish();
    }
}