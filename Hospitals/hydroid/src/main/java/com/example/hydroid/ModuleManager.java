package com.example.hydroid;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;

public class ModuleManager {
    private static final String TAG = "ModuleManager";
    private HashMap<String, HydroidModule> allModules;
    private HashMap<String, HydroidModule> sessionModules;
    private HydroidWebView webView;
    private Context parentContext;

    ModuleManager(HydroidWebView hydroidWebView, Context context) {

        this.webView = hydroidWebView;
        this.parentContext = context;
        allModules = new HashMap<String, HydroidModule>();
        sessionModules = new HashMap<String, HydroidModule>();
        initAllModules();

    }

    void initAllModules() {

        if(allModules != null) {
            allModules.put(Constants.HYDROID_BARCODE, new HydroidBarcode(webView, parentContext));
            allModules.put(Constants.HYDROID_FINGERPRINT, new HydroidFingerPrint(webView, parentContext));
            allModules.put(Constants.HYDROID_TOUCH,new HydroidTouch(webView,parentContext));

        }
    }

    @SuppressLint("JavascriptInterface")
    void addToSession(String moduleName) {
        if (allModules != null && allModules.containsKey(moduleName)) {
            HydroidModule module = allModules.get(moduleName);
            if(sessionModules != null) {
                sessionModules.put(moduleName, module);
                webView.addJavascriptInterface(module, module.getServiceName());

            }


        }

    }

    void removeFromSession(String moduleName) {
        if (sessionModules != null && sessionModules.containsKey(moduleName)) {
            sessionModules.remove(moduleName);
            webView.removeJavascriptInterface(moduleName);

        }
    }

    HydroidModule getModuleObject(String moduleName) {
        HydroidModule hydroidModuleObj = null;
        if (sessionModules != null && sessionModules.containsKey(moduleName)) {
            hydroidModuleObj = sessionModules.get(moduleName);
        }
        return hydroidModuleObj;
    }
}
