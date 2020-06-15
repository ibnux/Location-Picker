package com.ibnux.locationpicker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DirectionActivity extends AppCompatActivity {
    //https://www.google.com/maps/dir//-6.3763443,106.7190438/
    String mPerms[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    WebView webView;
    ProgressBar progressBar;
    String mapsUrl = "https://www.google.com/maps/",
            destination = "dir//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        webView.setWebChromeClient(chrome);
        webView.setWebViewClient(webViewClient);
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationDatabasePath(getFilesDir().getPath());
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36");
        settings.setUseWideViewPort(true);

        Intent i = getIntent();
        if (i.hasExtra("multiDirection")) {
            ArrayList<String> locations = i.getStringArrayListExtra("multiDirection");
            int count = locations.size();
            for(int n=0; n<count; n++) {
                destination += locations.get(n) + "/";
            }
            if(count>2) {
                mapsUrl += destination;
                openMaps(mapsUrl);
                finish();
            }
        } else if (i.hasExtra("lat") && i.hasExtra("lon")) {
            destination += i.getStringExtra("lat") + "," + i.getStringExtra("lon");
        }

        mapsUrl += destination;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(mPerms, 3312);
                return;
            } else {
                webView.loadUrl(mapsUrl);
            }
        } else {
            webView.loadUrl(mapsUrl);
        }
    }

    /**
     * Open Direction Maps
     *
     * @param lat String
     * @param lon String
     * @param cx  Context
     */
    public static void goTo(String lat, String lon, Context cx) {
        Intent i = new Intent(cx, DirectionActivity.class);
        i.putExtra("lat", lat);
        i.putExtra("lon", lon);
        cx.startActivity(i);
    }

    /**
     * Open Direction Maps
     *
     * @param lat double
     * @param lon double
     * @param cx  Context
     */
    public static void goTo(double lat, double lon, Context cx) {
        goTo(String.valueOf(lat), String.valueOf(lon), cx);
    }

    /**
     * Open Direction Maps with multiple destination
     * list.add("lat,lon");
     *
     * @param multiDirection arraylist
     */
    public static void goTo(List<String> multiDirection, Context cx) {
        Intent i = new Intent(cx, DirectionActivity.class);
        i.putStringArrayListExtra("multiDirection", (ArrayList<String>) multiDirection);
        cx.startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 3312) {
            //granted or not, still load maps, but user can't select my location
            webView.loadUrl(mapsUrl);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LocationPickerActivity.log("onPageStarted "+url);
            if (url.startsWith("intent")) {
                openMaps(url);
                view.stopLoading();
                view.goBack();
                return;
            }
            if (!url.startsWith(mapsUrl)) {
                view.stopLoading();
                view.goBack();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LocationPickerActivity.log("onPageFinished "+url);
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

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(DirectionActivity.this);
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Do you want to continue anyway?";

            builder.setTitle("SSL Certificate Error");
            builder.setMessage(message);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

    };

    WebChromeClient chrome = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar.isIndeterminate()) {
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
            callback.invoke(origin, true, true);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

    };

    public void openMaps(String url){
        if(url.startsWith("intent")){
            url = url.substring(url.indexOf("https:"));
        }
        LocationPickerActivity.log("openGoogleMaps "+url);
        try {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this,"Please install Google Maps",Toast.LENGTH_SHORT).show();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
            }
        }
    }
}