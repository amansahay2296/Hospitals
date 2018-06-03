package com.example.hydroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class HydroidBarcode extends HydroidModule {
    //Variable Declaration
    public static final String TAG = "HydroidBarcode";
    MessageCallback messageCallback;
    private boolean cameraPermission;
    private boolean storagePermission;
    private static String barcode_cache = null;
    Context context = ((Activity) getContext()).getApplicationContext();
    SurfaceHolder holder;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    TextView barcodeText;
    BarcodeDetector detector;
    private boolean scanned;


    //Constructor
    HydroidBarcode(HydroidWebView webview, Context context) {
        super(webview, context);
        messageCallback = new MessageCallback(webview,context);
        messageCallback.setCallbackName(getServiceName());
        webview.addJavascriptInterface(messageCallback,Constants.BarcodeCallback);
    }


    @JavascriptInterface
    public void readBarcode(){


        // agrs - Specifies the source of the barcode (whether it has to be scanned from the camera or fetched from gallery)
        // type - Specifies the type of barcode to be scanned (Whether QR code or Produ


                cameraPermission = hasPermission(Manifest.permission.CAMERA);

                if (cameraPermission) {
                    scanWithCamera();

                } else {
                    managePermission(Manifest.permission.CAMERA);

                }





    }


    public void scanWithCamera() {



        final SurfaceView cameraView = new SurfaceView(getContext());

        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWebView().setVisibility(View.INVISIBLE);
                RelativeLayout mainLayout = ((Activity) getContext()).findViewById(R.id.mainLayout);

                mainLayout.addView(cameraView);

                cameraView.setZOrderMediaOverlay(true);
                holder = cameraView.getHolder();




                barcode = new BarcodeDetector.Builder(((Activity) getContext()))
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();


                if (!barcode.isOperational()) {
                    Toast.makeText(getContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
                    ((Activity) getContext()).finish();
                }


                cameraSource = new CameraSource.Builder(getContext(), barcode)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedFps(24)
                        .setAutoFocusEnabled(true)
                        .setRequestedPreviewSize(1920, 1024)
                        .build();

                cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {
                        try {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                cameraSource.start(cameraView.getHolder());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        Log.d("surfaceDestroyed", "Called!");

                    }
                });
                barcode.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                        if (barcodes.size() > 0) {
                            String barcode_value = barcodes.valueAt(0).displayValue;

                            if (!barcode_value.toString().equals(barcode_cache)) {
                                scanned = true;
                                messageCallback.success(barcode_value, getServiceName());
                                ((Activity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {




                                        RelativeLayout mainLayout = ((Activity)getContext()).findViewById(R.id.mainLayout);
                                        mainLayout.removeView(cameraView);
                                        getWebView().setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                                if(scanned){
                                barcode_cache = null;
                                }
                                else{
                                    barcode_cache = barcode_value;
                                }
                        }





                    }
                });
            }
        });
    }









    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Constants.BARCODE_CAMERA_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    cameraPermission = true;

                    scanWithCamera();
                }
                else {
                    ModuleMessage moduleMessage = new ModuleMessage(ModuleMessage.Status.PERMISSION_NOT_GRANTED);
                    moduleMessage.setEncodedMessage("HydroidBarcode "+moduleMessage.getEncodedMessage());
                    messageCallback.sendModuleMessage(moduleMessage);
                    Toast.makeText(getContext(),"HydroidCamera : "+moduleMessage.getEncodedMessage().toString(),Toast.LENGTH_SHORT ).show();
                }
                break;


        }
    }


}
