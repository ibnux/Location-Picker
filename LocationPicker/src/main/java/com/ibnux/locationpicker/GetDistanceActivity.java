package com.ibnux.locationpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GetDistanceActivity extends AppCompatActivity {
    String mPerms[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    WebView webView;
    ProgressBar progressBar;

    String mapsUrl = "https://www.google.com/maps/",
            destination = "dir/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_distance);


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

        // Add from location
        if (i.hasExtra("fromLat") && i.hasExtra("fromLon")) {
            destination += i.getStringExtra("fromLat") + "," + i.getStringExtra("fromLon")+ "/";
        }else{
            destination += "/";
        }

        if (i.hasExtra("multiDirection")) {
            ArrayList<String> locations = i.getStringArrayListExtra("multiDirection");
            int count = locations.size();
            for(int n=0; n<count; n++) {
                destination += locations.get(n) + "/";
            }
        } else if (i.hasExtra("toLat") && i.hasExtra("toLon")) {
            destination += i.getStringExtra("toLat") + "," + i.getStringExtra("toLon");
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
     * get Distance from user location
     *
     * @param toLat String to latitude
     * @param toLon String to longitude
     * @param cx Context
     */
    public static void disTanceOf(String toLat, String toLon, int requestCode, Activity cx) {
        Intent i = new Intent(cx, GetDistanceActivity.class);
        i.putExtra("toLat", toLat);
        i.putExtra("toLon", toLon);
        cx.startActivityForResult(i,requestCode);
    }

    /**
     * get Distance from user location
     *
     * @param toLat double to latitude
     * @param toLon double to longitude
     * @param cx Context
     */
    public static void disTanceOf(double toLat, double toLon, int requestCode, Activity cx) {
        disTanceOf(String.valueOf(toLat), String.valueOf(toLon), requestCode, cx);
    }


    /**
     *
     * @param fromLat from latitude
     * @param fromLon from longitude
     * @param toLat to latitude
     * @param toLon to longitude
     * @param cx Context
     */
    public static void disTanceOf(String fromLat, String fromLon, String toLat, String toLon, int requestCode, Activity cx) {
        Intent i = new Intent(cx, GetDistanceActivity.class);
        i.putExtra("fromLat", fromLat);
        i.putExtra("fromLon", fromLon);
        i.putExtra("toLat", toLat);
        i.putExtra("toLon", toLon);
        cx.startActivityForResult(i,requestCode);
    }

    /**
     *
     * @param fromLat from latitude
     * @param fromLon from longitude
     * @param toLat to latitude
     * @param toLon to longitude
     * @param cx Context
     */
    public static void disTanceOf(double fromLat, double fromLon, double toLat, double toLon, int requestCode, Activity cx) {
        disTanceOf(String.valueOf(fromLat), String.valueOf(fromLon), String.valueOf(toLat), String.valueOf(toLon), requestCode, cx);
    }

    /**
     * Open Direction Maps with multiple destination
     * max 2 destination
     * list.add("lat,lon");
     *
     * @param multiDirection arraylist
     */
    public static void disTanceOf(List<String> multiDirection, int requestCode, Activity cx) {
        Intent i = new Intent(cx, GetDistanceActivity.class);
        i.putStringArrayListExtra("multiDirection", (ArrayList<String>) multiDirection);
        cx.startActivityForResult(i, requestCode);
    }

    /**
     * Open Direction Maps with multiple destination
     * max 2 destination
     * list.add("lat,lon");
     *
     * @param fromLat from latitude
     * @param fromLon from longitude
     * @param multiDirection arraylist
     */
    public static void disTanceOf(String fromLat, String fromLon, List<String> multiDirection, int requestCode, Activity cx) {
        Intent i = new Intent(cx, GetDistanceActivity.class);
        i.putExtra("fromLat", fromLat);
        i.putExtra("fromLon", fromLon);
        i.putStringArrayListExtra("multiDirection", (ArrayList<String>) multiDirection);
        cx.startActivityForResult(i, requestCode);
    }

    /**
     * Open Direction Maps with multiple destination
     * max 2 destination
     * list.add("lat,lon");
     *
     * @param fromLat from latitude
     * @param fromLon from longitude
     * @param multiDirection arraylist
     */
    public static void disTanceOf(double fromLat, double fromLon, List<String> multiDirection, int requestCode, Activity cx) {
        disTanceOf(String.valueOf(fromLat),String.valueOf(fromLon),multiDirection, requestCode, cx);
    }

    public void chooseLocation(View v){
        //TODO Everytime Google change maps design, this function need to change
        webView.evaluateJavascript("(function() { " +
                "var dis = document.getElementsByClassName('mapsLiteJsPanesDirectionsdetails__ml-directions-pane-header-time')[0].innerHTML;" +
                "" +
                "dis=dis.replace(/(<([^>]+)>)/gi, \"\")" +
                ".replace(/[^a-zA-Z0-9 .,(]/gi,'');" +
                "" +

                "time = dis.substring(0,dis.indexOf('(')).trim();" +
                "dis = dis.substring(dis.indexOf('(')+1);" +
                "var distance = dis.split(' ');" +
                "var result;" +
                "if(distance[1] == 'km'){" +
                "    result = distance[0].replace(',','.') * 1000;" +
                "}else if(distance[1] == 'm'){" +
                "    result = distance[0];" +
                "}" +
                "result+='|';" +
                "return result+time; })();", new ValueCallback<String>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onReceiveValue(String result) {
                result = result.replace("\"","");
                LocationPickerActivity.log("onReceiveValue "+result);
                try {
                    Intent i = getIntent();
                    i.putExtra("meters", result.substring(0, result.indexOf("|")));
                    i.putExtra("times", result.substring(result.indexOf("|") + 1));
                    setResult(RESULT_OK, i);
                }catch (Exception e){
                    Intent i = getIntent();
                    i.putExtra("meters", "failed");
                    i.putExtra("times", "failed");
                    setResult(RESULT_OK,i);
                }
                finish();
            }
        });
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(GetDistanceActivity.this);
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
}