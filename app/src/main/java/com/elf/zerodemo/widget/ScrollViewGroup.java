package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.elf.zero.utils.LogUtils;

/**
 * Scroll
 * Created by Lidong on 2018/1/25.
 */
public class ScrollViewGroup extends ViewGroup {

    private final static String TAG = ScrollViewGroup.class.getSimpleName();

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity,mMaximumVelocity,mTouchSlop;

    private int mContentHeight = 0;

    public ScrollViewGroup(Context context) {
        super(context);
        init();
    }

    public ScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mContentHeight = 0;
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                mContentHeight += getChildAt(i).getMeasuredHeight();
            }
        }

        LogUtils.v(TAG, "---onMeasure mContentHeight=" + mContentHeight);
        LogUtils.v(TAG, "---onMeasure getMeasuredHeight()=" + getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int height = 0;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                view.layout(0, height, view.getMeasuredWidth(), height + view.getMeasuredHeight());
                height += view.getMeasuredHeight();
            }
        }
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

                if (Math.abs(event.getY() - downY) > mTouchSlop) {
                    scrollBy(0, -(int) offsetY);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }

                super.dispatchTouchEvent(event);
                break;

            case MotionEvent.ACTION_UP:
                obtainVelocityTracker(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityY = mVelocityTracker.getYVelocity();

                if (Math.abs(velocityY) > mMinimumVelocity) {
                    mOverScroller.fling(0, getScrollY(), 0, -(int) velocityY
                            , 0, 0, 0, mContentHeight - getMeasuredHeight());
                    postInvalidate();
                } else {
                    super.dispatchTouchEvent(event);
                }

                releaseVelocityTracker();
                break;
        }


        return true;
    }

//    @Override
//    public boolean disto(MotionEvent event) {
////        LogUtils.v(TAG, "--onTouchEvent ");
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_POINTER_DOWN:
//            case MotionEvent.ACTION_DOWN:
//                downY = event.getY();
//                preY = event.getY();
//                obtainVelocityTracker(event);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                obtainVelocityTracker(event);
//                float offsetY = event.getY() - preY;
//                preY = event.getY();
//                scrollBy(0, -(int) offsetY);
//                break;
//
//            case MotionEvent.ACTION_UP:
//                obtainVelocityTracker(event);
//                mVelocityTracker.computeCurrentVelocity(1000);
//                float velocityY = mVelocityTracker.getYVelocity();
//
//                mOverScroller.fling(0, getScrollY(), 0, -(int) velocityY
//                        , 0, 0, 0, mContentHeight - getMeasuredHeight());
//                postInvalidate();
//
//                releaseVelocityTracker();
//                break;
//        }
//
//        super.onTouchEvent(event);
//        return true;
//    }

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

    private void init() {
        mOverScroller = new OverScroller(getContext());


        final ViewConfiguration configuration = ViewConfiguration.get(getContext());

        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

//        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                LogUtils.v(TAG,"--GestureDetector onDown");
////                if (mVelocityTracker == null) {
////                    mVelocityTracker = VelocityTracker.obtain();
////                } else {
////                    mVelocityTracker.clear();
////                }
////                mVelocityTracker.addMovement(e);
//                return false;
//            }
//
//            @Override
//            public void onShowPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
////                mVelocityTracker.addMovement(e2);
//                LogUtils.v(TAG,"--GestureDetector onScroll");
////                mOverScroller.startScroll(getScrollX(), getScrollY(), (int) distanceX, (int) distanceY);
//                scrollBy(0,(int) distanceY);
//                return false;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                LogUtils.v(TAG, "--GestureDetector onFling");
//                mOverScroller.fling(0, mOverScroller.getFinalY(), 0, (int) velocityY,
//                        0, 0, 0, mContentHeight - getMeasuredHeight());
//                postInvalidate();
//                return false;
//            }
//        });
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mOverScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());

            LogUtils.v(TAG, "---CurrVelocity" + mOverScroller.getCurrVelocity());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        } else {
            if(getScrollY() >= mContentHeight-getMeasuredHeight()) {
                LogUtils.v(TAG, "---computeScroll = end");
                if (mScrollFling != null) {
                    mScrollFling.fling(mOverScroller.getCurrVelocity());
                }
            }
        }
    }

    public ScrollFling mScrollFling;

    public interface ScrollFling {
        void fling(float velocity);
    }
}
