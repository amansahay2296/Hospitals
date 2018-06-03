package com.example.hydroid;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONException;

public class MessageCallback {
    private static final String TAG = "MessageCallback";
    private HydroidWebView webView;
    private Context parentContext;
    private String callbackName;

    public MessageCallback(HydroidWebView hydroidWebView,Context context) {
        this.webView = hydroidWebView;
        this.parentContext = context;
    }


    public void setCallbackName (String name){
        this.callbackName = name;
    }
    public String getCallbackName() {
        return callbackName;
    }

    public synchronized void sendModuleMessage(ModuleMessage moduleMessage) {



        if(moduleMessage.getStatusID()==0) {
            webView.addToModuleMessageQueue(moduleMessage);
        }
        notify();

        //Notify that Result has been added to the Module Message Queue

    }

    public void success(String result, String callbackID) {
        ModuleMessage moduleMessage = new ModuleMessage(ModuleMessage.Status.OK, result);
        moduleMessage.setCallbackID(callbackID);
        sendModuleMessage(moduleMessage);

    }

    public void error(String errorMessage, String callbackID) {
        ModuleMessage moduleMessage = new ModuleMessage(ModuleMessage.Status.ERROR, errorMessage);
        moduleMessage.setCallbackID(callbackID);
        sendModuleMessage(moduleMessage);
    }

    // This function is exposed to javascript for getting Result from Native

    @JavascriptInterface
    public synchronized String processResultFromQueue () throws InterruptedException, JSONException {

        ModuleMessage moduleMessage = null;
        String result = null;
        // Wait for Result to be loaded in the Module Message Queue

        wait();
        moduleMessage = webView.popFromModuleMessageQueue();

        if (moduleMessage != null && moduleMessage.getStatusID()== 0) {
            result = moduleMessage.getEncodedMessage();



        }
        return result;

    }
}
