package com.example.hydroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;

public class HydroidModule {
    private static final String TAG = "Module";
    private HydroidWebView webView;
    private String serviceName;
    private Context parentContext;



    HydroidModule(HydroidWebView hydroidWebView, Context context) {
        this.webView = hydroidWebView;
        this.parentContext = context;
        serviceName = this.getClass().getSimpleName().toString();
    }

    public HydroidWebView getWebView() {
        return webView;
    }
    public Context getContext() {
        return parentContext;
    }

    public String getServiceName() {
        return serviceName;

    }

    public boolean hasPermission(String permission) {

        boolean flag = false;
        if (ContextCompat.checkSelfPermission(parentContext, permission) == PackageManager.PERMISSION_GRANTED) {
            flag = true;
        }
        return flag;
    }

    public void managePermission(String permission) {
        int requestCode = 0;
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < Build.VERSION_CODES.M || hasPermission(permission)) {
            return;
        } else {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    requestCode = Constants.BARCODE_CAMERA_REQUEST_CODE;
                    break;
                case Manifest.permission.USE_FINGERPRINT :
                    requestCode = Constants.FINGERPRINT_REQUEST_CODE;
                    break;

            }
            if (requestCode != 0) {
                ActivityCompat.requestPermissions(((Activity) getContext()), new String[]{permission}, requestCode);
            }

        }



    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String source = null;
        HydroidModule moduleObject = null;

        switch (requestCode) {

            case Constants.BARCODE_CAMERA_REQUEST_CODE:
                source = Constants.HYDROID_BARCODE;
                break;
            case Constants.FINGERPRINT_REQUEST_CODE:
                source = Constants.HYDROID_FINGERPRINT;
                break;


        }
        if (source != null) {
            moduleObject = webView.getModuleObject(source);
        }

        if (moduleObject != null && (moduleObject instanceof HydroidBarcode || moduleObject instanceof HydroidFingerPrint)) {
            moduleObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) throws IOException {
        String source = null;
        HydroidModule moduleObject = null;

        if (source != null) {
            moduleObject = webView.getModuleObject(source);
        }


        if (moduleObject != null && (moduleObject instanceof HydroidBarcode)) {
            moduleObject.onActivityResult(requestCode, resultCode, data);

        }

    }

}
