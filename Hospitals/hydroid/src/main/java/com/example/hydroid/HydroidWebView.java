package com.example.hydroid;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class HydroidWebView extends WebView {

    private static final String TAG = "HydroidWebView";
    private WebSettings webSettings;
    private ModuleManager moduleManager;
    private ModuleMessageQueue moduleMessageQueue;
    private Context parentContext;
    private ProgressBar progressBar;

    public HydroidWebView(Context context) {
        super(context);
        parentContext = context;
        this.init();
    }

    public void init() {
        webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        progressBar = new ProgressBar(parentContext);

        this.setWebViewClient(new WebViewClient());
        this.setWebChromeClient(new WebChromeClient());



        moduleManager = new ModuleManager(this, parentContext);
        moduleMessageQueue = new ModuleMessageQueue(this,parentContext);
    }

    public void requestModule(String moduleName) {

        moduleManager.addToSession(moduleName);

    }

    public void releaseModule(String moduleName) {
        moduleManager.removeFromSession(moduleName);
    }

    public synchronized void addToModuleMessageQueue(ModuleMessage moduleMessage) {

        moduleMessageQueue.addToQueue(moduleMessage);

    }
    public synchronized ModuleMessage popFromModuleMessageQueue (){
        return moduleMessageQueue.popAndExecute();
    }



    public HydroidModule getModuleObject(String moduleName) {
        return moduleManager.getModuleObject(moduleName);

    }
}
