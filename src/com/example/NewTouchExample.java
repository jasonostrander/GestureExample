package com.example;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class NewTouchExample extends View {
    private float mScale = 1f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private List<Pointer> mPointers;
    private Paint mPaint;
    private float mFontSize;
    
    class Pointer {
        float x = 0;
        float y = 0;
        int index = -1;
        int id = -1;
    }
    
    public NewTouchExample(Context context) {
        super(context);
        mPointers = new ArrayList<Pointer>();
        for (int i = 0; i<5; i++) {
            mPointers.add(new Pointer());
        }
        
        mFontSize = 16 * getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);

        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(mScale*mFontSize);
        
        for (Pointer p : mPointers) {
            if (p.id != -1) {
                String text = "Index: " + p.index + " ID: " + p.id;
                canvas.drawText(text, p.x, p.y, mPaint);
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
        case MotionEvent.ACTION_MOVE:
            // clear previous pointers
            for (int i = 0; i<mPointers.size(); i++)
                mPointers.get(i).id = -1;

            // Now fill in the current pointers
            for (int i = 0; i<event.getPointerCount(); i++) {
                Pointer pointer = mPointers.get(i);
                pointer.index = i;
                pointer.id = event.getPointerId(i);
                pointer.x = event.getX(i);
                pointer.y = event.getY(i);
            }
            invalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
        }
        return true;
    }
    
    public class ZoomGesture extends GestureDetector.SimpleOnGestureListener {
        private boolean normal = true;
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (normal) {
                mScale = 3f;
            } else {
                mScale = 1f;
            }
            normal = !normal;
            invalidate();
            return true;
        }
    }
    
    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            invalidate();
            return true;
        }
    }
}
