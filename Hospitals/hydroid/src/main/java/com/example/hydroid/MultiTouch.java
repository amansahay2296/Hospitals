package com.example.hydroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MultiTouch extends View {
    private SparseArray<PointF> activePointers;
    private Paint mPaint;
    private String passCode;
    private int temp;
    MessageCallback messageCallback;
    Context context;
    MultiTouch current;
    HydroidWebView webView;

    public MultiTouch(Context context, @Nullable AttributeSet attrs, MessageCallback messageCallback,HydroidWebView webView) {
        super(context, attrs);
        this.messageCallback = messageCallback;
        this.context =context;
        this.webView = webView;
        current = this;
        initView();
        passCode = "";
    }

    void initView() {
        activePointers = new SparseArray<PointF>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                activePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE:  // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = activePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                activePointers.remove(pointerId);
                break;

        }
        invalidate();
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw all pointers
        for (int size = activePointers.size(), i = 0; i < size; i++) {
            PointF point = activePointers.valueAt(i);

            if (point != null)
                mPaint.setColor(Constants.colors[i % 9]);
            canvas.drawCircle(point.x, point.y, 60, mPaint);
        }
        if (activePointers.size() != 0) {
            Vibrator vb = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(1000);
        }
        if (activePointers.size() != 0) {
            temp = activePointers.size();
        }
        if (passCode.length() < 4 && activePointers.size() == 0 && temp != 0) {

            passCode += temp;
            temp = 0;
        }


        if (passCode.length() == 4) {
            messageCallback.success(passCode,"HydroidTouch");
            passCode.equals("");
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout mainLayout = ((Activity) context).findViewById(R.id.mainLayout);
                    mainLayout.removeView(current);
                    webView.setVisibility(VISIBLE);
                }
            });

        }


    }
}
