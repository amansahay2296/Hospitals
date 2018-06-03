package com.example.hydroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HydroidTouch extends HydroidModule   {
    MessageCallback messageCallback;
    View multiTouch;
    private SparseArray<PointF> activePointers;
    private Paint mPaint;
    private String passCode;
    private int temp;




    HydroidTouch(HydroidWebView hydroidWebView, Context context) {
        super(hydroidWebView, context);
        messageCallback = new MessageCallback(hydroidWebView,context);
        hydroidWebView.addJavascriptInterface(messageCallback,Constants.TouchCallback);
        activePointers = new SparseArray<PointF>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set painter color to a color you like
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }
    @JavascriptInterface
    public void generateSpecialPassword(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final RelativeLayout mainLayout = ((Activity) getContext()).findViewById(R.id.mainLayout);
                    multiTouch = new MultiTouch(getContext(),null,messageCallback,getWebView());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    multiTouch.setLayoutParams(layoutParams);


                    getWebView().setVisibility(View.INVISIBLE);
                    mainLayout.addView(multiTouch);

            }
        });

    }
    public void onDraw()
    {
        // draw all pointers

        Toast.makeText(getContext(),"onDraw is also called",Toast.LENGTH_SHORT).show();




    }



}
