package com.elf.zero.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 下拉刷新布局
 * Created by Lidong on 2017/12/23.
 */
public class PullRefreshLayout extends TouchEventLayout {

    private final static String TAG = PullRefreshLayout.class.getSimpleName();

    private final static int status_idle = 0;
    private final static int status_down_pull = 1;
    private final static int status_refreshing = 2;

    private final static int status_up_pull = 3;
    private final static int status_loading = 4;

    private View mHeader,mFooter,mContent;
    private PullListener mPullListener;
    private int mStatus=status_idle;

    public PullRefreshLayout(Context context) {
        super(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = getChildAt(0);

        setHeaderView();
        setFooterView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onDown(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onMove(MotionEvent ev, float offsetX, float offsetY) {
        if (mStatus == status_idle) {
            if (offsetY > 0 && mPullListener != null && mPullListener.canDownPull()) {
                mContent.offsetTopAndBottom((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_down_pull;
                return true;
            } else if (offsetY < 0 && mPullListener != null && mPullListener.canUpPull()) {
                mContent.offsetTopAndBottom((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_up_pull;
                return true;
            }
            return false;
        }
        if (mStatus == status_down_pull) {
            mContent.offsetTopAndBottom((int) offsetY);
            sendCancelEvent(ev);
            return true;
        }

        if (mStatus == status_up_pull) {
            mContent.offsetTopAndBottom((int) offsetY);
            sendCancelEvent(ev);
            return true;
        }
        return false;
    }

    @Override
    public boolean onUp(MotionEvent ev) {
        if (mStatus == status_down_pull || mStatus==status_up_pull) {
            ValueAnimator animator = ObjectAnimator.ofInt(mContent.getTop(), 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
//                    mContent.setTop((int) value);
                    mContent.offsetTopAndBottom(value - mContent.getTop());
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mStatus = status_idle;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
            return false;
        }

//        if (mStatus == status_up_pull) {
//            ValueAnimator animator = ObjectAnimator.ofFloat(mContent.getBottom(), getMeasuredHeight());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float value = (float) animation.getAnimatedValue();
//                    mContent.setBottom((int) value);
//                }
//            });
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mStatus = status_idle;
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//            animator.start();
//            return false;
//        }
        return false;
    }

    private void sendCancelEvent(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    public void setHeaderView() {
        TextView view = new TextView(getContext());
        view.setText("下拉刷新");
        view.setBackgroundColor(0x10000000);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 120));
        mHeader= view;
    }

    public void setFooterView() {
        TextView view = new TextView(getContext());
        view.setText("上啦加载");
        view.setBackgroundColor(0x10000000);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 120));
        mFooter = view;
    }

    public void setPullListener(PullListener listener) {
        mPullListener = listener;
    }

    public interface PullListener{
        boolean canDownPull();

        boolean canUpPull();

        boolean updateView();
    }
}
