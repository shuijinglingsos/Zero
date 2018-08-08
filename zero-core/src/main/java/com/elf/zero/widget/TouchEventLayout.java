package com.elf.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.elf.zero.utils.LogUtils;

/**
 * 触摸事件处理的控件
 * Created by Lidong on 2018/1/16.
 */
public abstract class TouchEventLayout extends ViewGroup implements TouchEventHelper.OnTouchEventListener {

    private TouchEventHelper mTouchEventHelper;

    public TouchEventLayout(Context context) {
        this(context, null);
    }

    public TouchEventLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTouchEventHelper = new TouchEventHelper();
        mTouchEventHelper.setOnTouchEventListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        outputLog("dispatchTouchEvent - "+ev.getAction());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                outputLog("dispatchTouchEvent - ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                outputLog("dispatchTouchEvent - ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                outputLog("dispatchTouchEvent - ACTION_UP");
                break;
        }
        return mTouchEventHelper.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        outputLog("onInterceptTouchEvent - "+ev.getAction());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                outputLog("onInterceptTouchEvent - ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                outputLog("onInterceptTouchEvent - ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                outputLog("onInterceptTouchEvent - ACTION_UP");
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        outputLog("onTouchEvent - "+event.getAction());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                outputLog("onTouchEvent - ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                outputLog("onTouchEvent - ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                outputLog("onTouchEvent - ACTION_UP");
                break;
        }

        //如果底层没有消费事件，这里需要消费事件，才能有后续时间分发
        return true; // super.onTouchEvent(event);
    }

    protected void outputLog(String msg) {
        LogUtils.v(this.getClass().getSimpleName(), msg);
    }
}
