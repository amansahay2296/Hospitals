package com.example.hydroid;

import android.graphics.Color;

public class Constants {



    public static final int BARCODE_CAMERA_REQUEST_CODE = 1;
    public static final int FINGERPRINT_REQUEST_CODE = 2;
    //HydroidFingerPrint
    public static final String SECRET_KEY = "vERYsecretKEY";
    public static final String FINGER_PRINT_DIALOG = "FingerPrintDialog";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";


    public static final String HYDROID_BARCODE = "HydroidBarcode";
    public static final String HYDROID_FINGERPRINT  ="HydroidFingerPrint";
    public static final String HYDROID_TOUCH = "HydroidTouch";
    public static final String TouchCallback = "TouchCallback";


    //Barcode

    //BArcode Mode
    public static final int DATA_MATRIX = 1;
    public static final int QR_CODE = 2;
    public static final String PRODUCT_MODE = "PRODUCT_MODE";
    public static final String QR_MODE = "QR_MODE";

    //Message Callback
    public static final String MessageCallback = "MessageCallback";
    public static final String BarcodeCallback = "BarcodeCallback";
    public static final String FingerPrintCallback = "FingerPrintCallback";
    public static final String callbackID = "callbackID";
    public static final String result = "result";
    public static final int [] colors = {Color.BLUE,Color.MAGENTA,Color.GREEN,Color.BLACK,Color.CYAN,Color.GRAY
                                            ,Color.RED,Color.DKGRAY,Color.LTGRAY,Color.YELLOW};


}
