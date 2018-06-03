package com.example.hydroid;

import android.content.Context;

import java.util.LinkedList;
import java.util.Queue;

public class ModuleMessageQueue {
    private static final String TAG = "ModuleMessageQueue";
    private Queue<ModuleMessage> queue;
    HydroidWebView webView;
    Context parentContext;

    public ModuleMessageQueue(HydroidWebView hydroidWebView,Context context) {
        queue = new LinkedList<>();
        this.webView = hydroidWebView;
        this.parentContext = context;
    }

    void addToQueue(ModuleMessage moduleMessage) {

        queue.add(moduleMessage);

    }

    ModuleMessage popAndExecute() {
        ModuleMessage moduleMessage = null;
        if (!queue.isEmpty()) {
            moduleMessage = queue.remove();


        }


        return moduleMessage;

    }

}
