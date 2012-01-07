package com.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class TouchExample extends View {
    private Drawable mDrawable;
    private float dx;
    private float dy;
    private int mWidth;
    private int mHeight;
    private int mPointer = -1;
    private float mScale = 1f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    public TouchExample(Context context) {
        super(context);
        mDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mWidth = mDrawable.getIntrinsicWidth()/2;
        mHeight = mDrawable.getIntrinsicHeight()/2;
        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(mScale, mScale);
        mDrawable.draw(canvas);
        canvas.restore();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        
        // index 0 is primary pointer
        int pointer = event.getPointerId(0);
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            mPointer = pointer;
        case MotionEvent.ACTION_MOVE:
            if (mPointer != pointer)
                break;
            
            dx = event.getX() - mScale*mWidth;
            dy = event.getY() - mScale*mHeight;
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mPointer = -1;
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
