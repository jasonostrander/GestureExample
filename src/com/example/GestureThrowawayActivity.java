package com.example;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GestureThrowawayActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        NewTouchExample_JBL view = new NewTouchExample_JBL(this);
      NewTouchExample view = new NewTouchExample(this);
        
//        FirstPassExample view = new FirstPassExample(this);
//        TouchExample view = new TouchExample(this);
//        TouchIdExample view = new TouchIdExample(this);

        setContentView(view);
        
//        mSwipeDetector = new GestureDetector(this, new SwipeDetector());
//        MyAdapter adapter = new MyAdapter(this, 0);
//        setListAdapter(adapter);
    }
    
    
    private GestureDetector mSwipeDetector;
    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                float velocityX, float velocityY) {
            Toast.makeText(getApplicationContext(), "Swipe detected", Toast.LENGTH_LONG).show();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    
    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return 20;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView.inflate(getContext(), R.layout.row, null);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setText("Testing: " + position);
            tv.setOnTouchListener(new OnTouchListener() {
                
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mSwipeDetector.onTouchEvent(event);
                    return false;
                }
            });
            return v;
        }
    }
    
    public static class MyTouchExample extends View {
        private Drawable mDrawable;
        private float dx;
        private float dy;
        private float mScale = 1;
        private ScaleGestureDetector mScaleGestureDetector;
        private GestureDetector mGestureDetector;
        
        public MyTouchExample(Context context) {
            super(context);
            mDrawable = getResources().getDrawable(R.drawable.circle);
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleDetector());
            mGestureDetector = new GestureDetector(context, new SwipeDetector());
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
        
        private int mPointer = -1;
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
//            Log.v("jason", "onTouchEvent");
            // always call the GestureDetector.onTouchEvent in
            // your view onTouchEvent callback
            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
            int pointer = event.getPointerId(0);
            
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mPointer == -1)
                    mPointer = pointer;
            case MotionEvent.ACTION_MOVE:
                if (mPointer != pointer)
                    break;
                
                dx = event.getX() - 100*mScale;
                dy = event.getY() - 100*mScale;
                
                invalidate();
                break;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPointer = -1;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int index = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    if (index == mPointer) {
                        
                    }
                break;
            }
            return true;
        }
        
        private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
            private boolean normal = true;
            
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.v("jason", "onDoubleTap");
                if (normal) {
                    mScale = 3;
                } else {
                    mScale = 1f;
                }
                normal = !normal;
                invalidate();
                return true;
            }
            
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                    float velocityX, float velocityY) {
                float x = e2.getX() * velocityX/10000;
                float y = e2.getY() * velocityY/10000;
                Log.v("jason", "getX = " + e2.getX() + " vel=" + velocityX);
                TranslateAnimation ta = new TranslateAnimation(0, x, 0, y);
                ta.setDuration(1000);
                
                MyTouchExample.this.startAnimation(ta);
                return true;
            }
        }

        private class ScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScale *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScale= Math.max(0.1f, Math.min(mScale, 5.0f));

                invalidate();
                return true;
            }
        }

    }
    /**/
}