package com.elf.zero.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.elf.zero.utils.LogUtils;

/**
 * 下拉刷新布局
 * Created by Lidong on 2017/12/23.
 */
public class PullRefreshLayout extends TouchEventLayout {

    private final static String TAG = PullRefreshLayout.class.getSimpleName();

    private final static int status_idle = 0;
    private final static int status_down_pull = 1;
    private final static int status_up_pull = 2;

    private View mHeader, mFooter, mContent;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mStatus = status_idle;
    private boolean mRefreshing,mLoading;
    private int mTop;

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
        mContent.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
//        mContent.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                LogUtils.v(TAG, "--onLayoutChange--");
//                mHeader.offsetTopAndBottom(top - oldTop);
//                mFooter.offsetTopAndBottom(top - oldTop);
//            }
//        });

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

        LogUtils.v(TAG, "--onLayout--");
        mHeader.layout(0, mTop - mHeader.getMeasuredHeight(), mHeader.getMeasuredWidth(), mTop);
        mContent.layout(0, mTop, mContent.getMeasuredWidth(), mTop+getMeasuredHeight());
        mFooter.layout(0, mTop + getMeasuredHeight(), mFooter.getMeasuredWidth(), mTop+getMeasuredHeight() + mFooter.getMeasuredHeight());

//        if (getChildCount() > 0) {
//            for (int i = 0; i < getChildCount(); i++) {
//                View view = getChildAt(i);
//                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//            }
//        }
    }

    public void startRefresh() {
        if (mRefreshing || mLoading) {
            return;
        }
        mRefreshing = true;
        ((TextView) mHeader).setText("正在刷新");
        viewAnim(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mStatus = status_idle;
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.refresh();
                }
            }
        }, mContent.getTop(), mHeader.getMeasuredHeight());
    }

    public void stopRefresh() {
        viewAnim(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshing = false;
            }
        }, mContent.getTop(), 0);
    }

    public void stopLoadMore() {
        viewAnim(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading = false;
            }
        }, mContent.getTop(), 0);
    }

    @Override
    public boolean onDown(MotionEvent ev) {

        return false;
    }

    @Override
    public boolean onMove(MotionEvent ev, float offsetX, float offsetY) {
        //正在刷新时
        if(mRefreshing) {
            if (mStatus == status_idle) {
                if (offsetY > 0 && mOnRefreshListener != null && mOnRefreshListener.canPull()) {
                    offsetContent((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_down_pull;
                    return true;
                }
                if (offsetY < 0 && mContent.getTop() > 0) {
                    offsetContent((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_down_pull;
                    return true;
                }
                return false;
            }

            if (mStatus == status_down_pull) {
                if (mContent.getTop() + offsetY < 0) {
                    offsetY = 0 - mContent.getTop();
                }
                offsetContent((int) offsetY);
                if (mContent.getTop() == 0) {
                    sendDownEvent(ev);
                    mStatus = status_idle;
                }
                return true;
            }
            return false;
        }

        //正在加载时
        if(mLoading) {
            if (mStatus == status_idle) {
                if (offsetY > 0 && mContent.getBottom() < getMeasuredHeight()) {
                    offsetContent((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_up_pull;
                    return true;
                }
                if (offsetY < 0 && mOnLoadMoreListener != null && mOnLoadMoreListener.canPull()) {
                    offsetContent((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_up_pull;
                    return true;
                }
                return false;
            }

            if (mStatus == status_up_pull) {
                if (mContent.getBottom() + offsetY > getMeasuredHeight()) {
                    offsetY = getMeasuredHeight() - mContent.getBottom();
                }
                offsetContent((int) offsetY);
                if (mContent.getBottom() == getMeasuredHeight()) {
                    sendDownEvent(ev);
                    mStatus = status_idle;
                }

                return true;
            }
        }

        //正常情况下
        if (mStatus == status_idle) {
            if (offsetY > 0 && mOnRefreshListener != null && mOnRefreshListener.canPull()) {
                offsetContent((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_down_pull;
                return true;
            } else if (offsetY < 0 && mOnLoadMoreListener != null && mOnLoadMoreListener.canPull()) {
                offsetContent((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_up_pull;
                return true;
            }
            return false;
        }

        if (mStatus == status_down_pull) {
            if (mContent.getTop() + offsetY < 0) {
                offsetY = 0 - mContent.getTop();
            }
            offsetContent((int) offsetY);
            if (mContent.getTop() == 0) {
                sendDownEvent(ev);
                mStatus = status_idle;
            }
            return true;
        }

        if (mStatus == status_up_pull) {
            if (mContent.getBottom() + offsetY > getMeasuredHeight()) {
                offsetY = getMeasuredHeight() - mContent.getBottom();
            }
            offsetContent((int) offsetY);
            if (mContent.getBottom() == getMeasuredHeight()) {
                sendDownEvent(ev);
                mStatus = status_idle;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onUp(MotionEvent ev) {
        if (mStatus == status_down_pull) {
            if (canRefresh()) {
                mRefreshing = true;
                ((TextView) mHeader).setText("正在刷新");
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.refresh();
                        }
                    }
                }, mContent.getTop(), mHeader.getMeasuredHeight());
            } else {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                    }
                }, mContent.getTop(), 0);
            }
            return false;
        }

        if (mStatus == status_up_pull) {
            if (canLoadMore()) {
                mLoading = true;
                ((TextView) mFooter).setText("正在加载");
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.loadMore();
                        }
                    }
                }, mContent.getTop(), -mHeader.getMeasuredHeight());
            } else {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                    }
                }, mContent.getTop(), 0);
            }
            return false;
        }

        return false;
    }

    /**
     * view动画
     * @param endListener 结束监听
     * @param values 动画区间值
     */
    private void viewAnim(final OnClickListener endListener, int... values) {
        ValueAnimator animator = ObjectAnimator.ofInt(values);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int offset = value - mContent.getTop();
                offsetContent(offset);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (endListener != null) {
                    endListener.onClick(null);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 是否能刷新
     * @return true可以 false不可以
     */
    private boolean canRefresh(){
        return mContent.getTop()>mHeader.getMeasuredHeight();
    }

    /**
     * 是否能加载更多
     * @return true可以 false不可以
     */
    private boolean canLoadMore() {
        return mContent.getBottom() + mFooter.getMeasuredHeight() < getMeasuredHeight();
    }

    /**
     * 发送取消事件
     * @param ev event
     */
    private void sendCancelEvent(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    /**
     * 发送按下事件
     * @param ev event
     */
    private void sendDownEvent(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(ev);
    }

    public void setHeaderView() {
        TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        view.setText("下拉刷新");
        view.setBackgroundColor(0x10000000);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 120));
        mHeader = view;
    }

    public void setFooterView() {
        TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        view.setText("上拉加载");
        view.setBackgroundColor(0x10000000);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 120));
        mFooter = view;
    }

    /**
     * 移动内容控件
     *
     * @param offset 偏移量
     */
    private void offsetContent(int offset) {
        mContent.offsetTopAndBottom(offset);
        mHeader.offsetTopAndBottom(offset);
        mFooter.offsetTopAndBottom(offset);

        mTop=mContent.getTop();

        if (mRefreshing || mLoading) {
            return;
        }

        if (canRefresh()) {
            ((TextView) mHeader).setText("松手刷新");
        } else {
            ((TextView) mHeader).setText("下拉刷新");
        }

        if (canLoadMore()) {
            ((TextView) mFooter).setText("松手加载");
        } else {
            ((TextView) mFooter).setText("上拉加载");
        }
    }

    /**
     * 设置刷新监听
     * @param listener 监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    /**
     * 设置加载更多监听
     * @param listener 监听
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public interface OnRefreshListener {
        boolean canPull();

        void refresh();
    }

    public interface OnLoadMoreListener {
        boolean canPull();

        void loadMore();
    }
}
