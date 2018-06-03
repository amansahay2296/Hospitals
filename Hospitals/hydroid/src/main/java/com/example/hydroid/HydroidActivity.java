package com.example.hydroid;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;

import java.io.IOException;

public class HydroidActivity extends AppCompatActivity {
    private final static String TAG = "HydroidActivity";
    private HydroidWebView webView;
    private HydroidModule hydroidModuleObj;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create a basic layout and launch the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main_layout);

        RelativeLayout relativeLayout = findViewById(R.id.mainLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        webView = new HydroidWebView(this);
        webView.setLayoutParams(layoutParams);
        relativeLayout.addView(webView);





        // Request  modules for the WebView
        webView.requestModule(Constants.HYDROID_BARCODE);
        webView.requestModule(Constants.HYDROID_FINGERPRINT);
        webView.requestModule(Constants.HYDROID_TOUCH);



        //Create an instance of Hydroid Module for Managing Permissions Result
        hydroidModuleObj = new HydroidModule(webView, this);

        // Load WebView with a  test Page
        webView.loadUrl("http://209.97.130.224:5000/doc");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (hydroidModuleObj != null) {
            try {
                hydroidModuleObj.onActivityResult(requestCode, resultCode, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (hydroidModuleObj != null) {
            hydroidModuleObj.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


}
