package com.elf.zero.widget;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * 触摸事件帮助类
 * Created by Lidong on 2017/1/12.
 */
public class TouchEventHelper {

    private PointF mDownPoint = new PointF();
    private PointF mLastPoint = new PointF();
    private OnTouchEventListener mOnTouchEventListener;

    /**
     * 拦截触摸时间
     *
     * @param ev event
     * @return true 拦截，false 不拦截
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPoint.set(ev.getX(), ev.getY());
                mLastPoint.set(ev.getX(), ev.getY());
                if (mOnTouchEventListener != null) {
                    return mOnTouchEventListener.onDown(ev);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float offsetX = ev.getX() - mLastPoint.x;
                float offsetY = ev.getY() - mLastPoint.y;
                mLastPoint.set(ev.getX(), ev.getY());
                if (mOnTouchEventListener != null) {
                    return mOnTouchEventListener.onMove(ev, offsetX, offsetY);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mOnTouchEventListener != null) {
                    return mOnTouchEventListener.onUp(ev);
                }
                break;
        }
        return false;
    }

    /**
     * 设置触摸监听
     *
     * @param listener 监听
     */
    public void setOnTouchEventListener(OnTouchEventListener listener) {
        mOnTouchEventListener = listener;
    }

    /**
     * 触摸监听接口
     */
    public interface OnTouchEventListener {
        /**
         * 按下
         *
         * @param ev ev
         * @return true 拦截，false 不拦截
         */
        boolean onDown(MotionEvent ev);

        /**
         * 移动
         *
         * @param ev      ev
         * @param offsetX x方向偏移量
         * @param offsetY y方向偏移量
         * @return true 拦截，false 不拦截
         */
        boolean onMove(MotionEvent ev, float offsetX, float offsetY);

        /**
         * 抬起
         *
         * @param ev ev
         * @return true 拦截，false 不拦截
         */
        boolean onUp(MotionEvent ev);
    }
}
