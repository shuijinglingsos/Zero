package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.OverScroller;

import com.elf.zero.utils.LogUtils;

/**
 * Created by Lidong on 2018/1/26.
 */
public class NestedScrollingWebView extends WebView {

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity,mMaximumVelocity,mTouchSlop;
    private boolean mBoolean;

    public NestedScrollingWebView(Context context) {
        super(context);
        init();
    }

    public NestedScrollingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mOverScroller = new OverScroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());

        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private float downY, preY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        return super.dispatchTouchEvent(ev);

        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                preY = event.getY();
                obtainVelocityTracker(event);
                super.dispatchTouchEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                obtainVelocityTracker(event);
                float offsetY = event.getY() - preY;
                preY = event.getY();

//                if (Math.abs(event.getY() - downY) > mTouchSlop) {
//                    scrollBy(0, -(int) offsetY);
//                    event.setAction(MotionEvent.ACTION_CANCEL);
//                }

                super.dispatchTouchEvent(event);
                break;

            case MotionEvent.ACTION_UP:
                obtainVelocityTracker(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityY = mVelocityTracker.getYVelocity();

                if (Math.abs(velocityY) > mMinimumVelocity) {
                    mOverScroller.fling(0, getScrollY(), 0, -(int) velocityY
                            , 0, 0, 0, (computeVerticalScrollRange()-getMeasuredHeight()));
                    mBoolean =true;
//                    postInvalidate();
                }
//                else {
                    super.dispatchTouchEvent(event);
//                }

                releaseVelocityTracker();
                break;
        }


        return true;
    }

//    @Override
//    public void flingScroll(int vx, int vy) {
//        super.flingScroll(vx, vy);
//
//        LogUtils.v("tag", "--flingScroll--");
//
//        mOverScroller.fling(0, getScrollY(), vx, vy, 0, 0, 0, computeVerticalScrollRange());
//    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        LogUtils.v("tag", "--computeScroll," + mOverScroller.getCurrVelocity());
        if (!mOverScroller.computeScrollOffset()) {

            LogUtils.v("tag", "--computeScroll=" + getScrollY() + "," + (computeVerticalScrollRange() - getMeasuredHeight()));

            if (mBoolean && getScrollY() >= (computeVerticalScrollRange() - getMeasuredHeight())) {
                mBoolean = false;
                LogUtils.v("tag", "---computeScroll = end," + mOverScroller.getCurrVelocity());
                if (mScrollFling != null) {
                    mScrollFling.fling(mOverScroller.getCurrVelocity()/3);
                }
            }
        }
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public ScrollFling mScrollFling;

    public interface ScrollFling {
        void fling(float velocity);
    }
}
