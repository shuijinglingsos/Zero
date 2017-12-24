package com.elf.zero.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.elf.zero.utils.LogUtils;

/**
 * 下拉刷新布局
 * Created by Lidong on 2017/12/23.
 */
public class PullRefreshLayout extends FrameLayout {

    private final static String TAG = PullRefreshLayout.class.getSimpleName();
    private final static int STATUS_NORMAL = 1;
    private final static int STATUS_REFRESH = 2;
    private final static int STATUS_COMPLETE = 3;

    private ViewDragHelper mViewDragHelper;
    private PullRefreshInterface mPullRefreshInterface;
    private TextView mRefreshHeader;
    private int mStatus = STATUS_NORMAL;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mRefreshHeader = new TextView(getContext());
        mRefreshHeader.setPadding(10, 10, 10, 10);
        mRefreshHeader.setText("下拉刷新");
//        mRefreshHeader.setTextColor(Color.WHITE);
        mRefreshHeader.setGravity(Gravity.CENTER);
        addView(mRefreshHeader, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                LogUtils.v(TAG, child.toString());
                return child == getChildAt(1);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                if (releasedChild.getTop() > mRefreshHeader.getMeasuredHeight()) {
                    mStatus = STATUS_REFRESH;
                    mRefreshHeader.setText("正在刷新");
                    if (mPullRefreshInterface != null) {
                        mPullRefreshInterface.refresh();
                    }
                }

                if (releasedChild.getTop() > mRefreshHeader.getMeasuredHeight()) {
                    mViewDragHelper.settleCapturedViewAt(0, mRefreshHeader.getMeasuredHeight());
                } else {
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                }

                ViewCompat.postInvalidateOnAnimation(PullRefreshLayout.this);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                LogUtils.v(TAG, "onViewPositionChanged: top=" + top);
                mRefreshHeader.offsetTopAndBottom(dy);
                if (mStatus == STATUS_NORMAL) {
                    if (top > mRefreshHeader.getMeasuredHeight()) {
                        mRefreshHeader.setText("松手刷新");
                    } else {
                        mRefreshHeader.setText("下拉刷新");
                    }
                }

//                if (changedView.getBottom() > getMeasuredHeight()) {
                    changedView.layout(left, top, changedView.getRight(), changedView.getBottom() - dy);
//                }
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top < 0 ? 0 : top - (dy / 2);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return child.getMeasuredHeight();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mStatus == STATUS_COMPLETE) {
            getChildAt(1).layout(0, mRefreshHeader.getMeasuredHeight(),
                    getChildAt(1).getMeasuredWidth(),
                    getChildAt(1).getMeasuredHeight());
            return;
        }
        mRefreshHeader.layout(0, -mRefreshHeader.getMeasuredHeight(), mRefreshHeader.getMeasuredWidth(), 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mStatus == STATUS_NORMAL
                && mPullRefreshInterface.isCanPull()
                && mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(PullRefreshLayout.this);
        } else {
            if (mStatus == STATUS_COMPLETE) {
                mStatus = STATUS_NORMAL;
            }
        }
    }

    public void stopRefresh() {
        if (mStatus == STATUS_REFRESH) {
            mStatus = STATUS_COMPLETE;
            mRefreshHeader.setText("完成刷新");
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewDragHelper.smoothSlideViewTo(getChildAt(1), 0, 0);
                    invalidate();
//                    ViewCompat.postInvalidateOnAnimation(PullRefreshLayout.this);
                }
            }, 300);
        }
    }

    public void setPullRefreshInterface(PullRefreshInterface value) {
        mPullRefreshInterface = value;
    }

    public interface PullRefreshInterface {
        boolean isCanPull();

        void refresh();
    }
}
