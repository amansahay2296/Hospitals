package com.example.hydroid;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import android.security.keystore.KeyProperties;
import android.webkit.JavascriptInterface;

import android.widget.Toast;


import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class HydroidFingerPrint extends HydroidModule {
    private FingerprintManager fingerprintManager;
    private boolean fingerPrintPermission;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private KeyguardManager keyguardManager;
    MessageCallback messageCallback;

    HydroidFingerPrint(HydroidWebView hydroidWebView, Context context) {
        super(hydroidWebView, context);
        messageCallback = new MessageCallback(hydroidWebView, context);
        messageCallback.setCallbackName(getServiceName());
        hydroidWebView.addJavascriptInterface(messageCallback, Constants.FingerPrintCallback);

        try {
            keyStore = KeyStore.getInstance(Constants.ANDROID_KEY_STORE);

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, Constants.ANDROID_KEY_STORE);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }


        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }


    }

    @JavascriptInterface
    public void scanFingerPrint() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            messageCallback.error("Feature not supported by SDK", getServiceName());
            return;
        }
        fingerPrintPermission = hasPermission(Manifest.permission.USE_FINGERPRINT);
        if (fingerPrintPermission) {
            launchFingerPrintDialog();
        } else {
            managePermission(Manifest.permission.USE_FINGERPRINT);
        }
    }

    void launchFingerPrintDialog() {

        keyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getContext().getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager.isHardwareDetected()) {
            if (keyguardManager.isKeyguardSecure()) {
                HydroidFingerPrintDialog fragment = new HydroidFingerPrintDialog();
                fragment.show(((Activity) getContext()).getFragmentManager(), Constants.FINGER_PRINT_DIALOG);

            } else {
                Toast.makeText(getContext(), "Please enable LockScreen Security in your Device Settings ", Toast.LENGTH_SHORT).show();
                messageCallback.error("Secure LockScreen", getServiceName());
            }
        } else {

            Toast.makeText(getContext(), "This device does not have Fingerprint Sensor", Toast.LENGTH_SHORT).show();
            messageCallback.error("Hardware not found", getServiceName());
        }

    }
}