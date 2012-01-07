package com.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchIdExample extends View {
    private Drawable mDrawable;
    private float dx;
    private float dy;
    private int mWidth;
    private int mHeight;
    private int mPointer = -1;
    private float mScale = 1f;

    public TouchIdExample(Context context) {
        super(context);
        mDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mWidth = mDrawable.getIntrinsicWidth()/2;
        mHeight = mDrawable.getIntrinsicHeight()/2;
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
        // index 0 is primary pointer
        int pointer = event.getPointerId(0);
        for (int i = 0; i<event.getPointerCount(); i++) {
            Log.v("jason", i + ": " + event.getPointerId(i));
        }
        dx = event.getX() - mWidth;
        dy = event.getY() - mHeight;
        invalidate();
        return true;
    }
}
