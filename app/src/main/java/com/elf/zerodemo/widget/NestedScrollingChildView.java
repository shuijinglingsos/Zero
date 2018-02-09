package com.elf.zerodemo.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import com.elf.zero.utils.LogUtils;

/**
 * 滑动签到子控件
 * Created by Lidong on 2018/1/24.
 */
public class NestedScrollingChildView extends WebView implements NestedScrollingChild {

    public static final String TAG = NestedScrollingChildView.class.getSimpleName();

    public static final int UP = 1;
    public static final int DOWN = -1;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private final int mTouchSlop;
    private final int mMinimumVelocity;
    private final int mMaximumVelocity;
    private int direction = DOWN; // TODO 还需要同步到父布局的方向
    private int mLastMotionY;
    private int mNestedYOffset;
    private NestedScrollingChildHelper mChildHelper;
    private VelocityTracker mVelocityTracker;
    private boolean allowFly;
    private int downY;
    private float mDownY = -1;

    public NestedScrollingChildView(Context context) {
        this(context, null);
    }

    public NestedScrollingChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mChildHelper = new NestedScrollingChildHelper(this); // 辅助类
        setNestedScrollingEnabled(true); // 设置支持 NestedScrolling

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean eventAddedToVelocityTracker = false;

        final int action = MotionEventCompat.getActionMasked(event);
        final int actionIndex = MotionEventCompat.getActionIndex(event);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                allowFly = false;
                downY = (int) event.getRawY();
                // 开始 NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                int dy = -(moveY - downY); //滚动方法的方向跟坐标是相反的，所以这里要加一个负号
                downY = moveY;
                //在consumed 中就是父类滑动的距离，
                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1]; // 减去父类消费的距离
                    scrollBy(0, dy); // 剩下的自己消费
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mVelocityTracker.addMovement(event);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int mScrollPointerId = MotionEventCompat.getPointerId(event, actionIndex);
                float vY = -VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
                // 产生 fling 事件
                if (Math.abs(vY) > mMinimumVelocity && !dispatchNestedPreFling(0, vY)) {
//                    dispatchNestedFling(0, vY, true);
                    logi("dispatchNestedFling");
                }
                resetTouch();
                break;
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(event);
        }
        return super.onTouchEvent(event);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        LogUtils.v(TAG, "--computeScroll");
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        stopNestedScroll();
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public void log(String s) {
        LogUtils.v(TAG, s);
    }

    public void logi(String s) {
        LogUtils.v(TAG, s);
    }

    public void logw(String s) {
        LogUtils.v(TAG, s);
    }
}