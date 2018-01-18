package com.elf.zero.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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

    private PullUIHandler mHeaderUIHandler, mFooterHandler;

    private View mHeaderView, mFooterView, mContentView;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mStatus = status_idle;
    private boolean mRefreshing, mLoading;
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
        mContentView = getChildAt(0);

        DefaultPullRefreshHeader header = new DefaultPullRefreshHeader(getContext());
        DefaultPullRefreshHeader footer = new DefaultPullRefreshHeader(getContext());
        setHeaderAndFooterView(header, footer);
        setHeaderAndFooterUIHandler(header, footer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        LogUtils.v(TAG, "--onLayout--");
        if (mHeaderView != null) {
            mHeaderView.layout(0, mTop - mHeaderView.getMeasuredHeight(), mHeaderView.getMeasuredWidth(), mTop);
        }
        mContentView.layout(0, mTop, mContentView.getMeasuredWidth(), mTop + getMeasuredHeight());
        if (mHeaderUIHandler != null) {
            mFooterView.layout(0, mTop + getMeasuredHeight(), mFooterView.getMeasuredWidth(), mTop + getMeasuredHeight() + mFooterView.getMeasuredHeight());
        }
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
        mHeaderUIHandler.onRefreshBegin();  //((TextView) mHeaderView).setText("正在刷新");
        viewAnim(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.refresh();
                }
            }
        }, mContentView.getTop(), mHeaderView.getMeasuredHeight());
    }

    public void startLoadMore() {
        if (mRefreshing || mLoading) {
            return;
        }

        mLoading = true;
        mFooterHandler.onRefreshBegin(); // ((TextView) mFooterView).setText("正在加载");
        viewAnim(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.loadMore();
                }
            }
        }, mContentView.getTop(), -mFooterView.getMeasuredHeight());
    }

    public void stopRefresh() {
        mHeaderUIHandler.onRefreshComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefreshing = false;
                    }
                }, mContentView.getTop(), 0);
            }
        }, 500);
    }

    public void stopLoadMore() {
        mFooterHandler.onRefreshComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoading = false;
                    }
                }, mContentView.getTop(), 0);
            }
        }, 500);
    }

    @Override
    public boolean onDown(MotionEvent ev) {

        return false;
    }

    @Override
    public boolean onMove(MotionEvent ev, float offsetX, float offsetY) {

        offsetY /= 2;

        //正在刷新时
        if (mRefreshing) {
            if (mStatus == status_idle) {
                if (offsetY > 0 && mOnRefreshListener != null && mOnRefreshListener.canPull()) {
                    offsetView((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_down_pull;
                    return true;
                }
                if (offsetY < 0 && mContentView.getTop() > 0) {
                    offsetView((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_down_pull;
                    return true;
                }
                return false;
            }

            if (mStatus == status_down_pull) {
                if (mContentView.getTop() + offsetY < 0) {
                    offsetY = 0 - mContentView.getTop();
                }
                offsetView((int) offsetY);
                if (mContentView.getTop() == 0) {
                    sendDownEvent(ev);
                    mStatus = status_idle;
                }
                return true;
            }
            return false;
        }

        //正在加载时
        if (mLoading) {
            if (mStatus == status_idle) {
                if (offsetY > 0 && mContentView.getBottom() < getMeasuredHeight()) {
                    offsetView((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_up_pull;
                    return true;
                }
                if (offsetY < 0 && mOnLoadMoreListener != null && mOnLoadMoreListener.canPull()) {
                    offsetView((int) offsetY);
                    sendCancelEvent(ev);
                    mStatus = status_up_pull;
                    return true;
                }
                return false;
            }

            if (mStatus == status_up_pull) {
                if (mContentView.getBottom() + offsetY > getMeasuredHeight()) {
                    offsetY = getMeasuredHeight() - mContentView.getBottom();
                }
                offsetView((int) offsetY);
                if (mContentView.getBottom() == getMeasuredHeight()) {
                    sendDownEvent(ev);
                    mStatus = status_idle;
                }

                return true;
            }
        }

        //正常情况下
        if (mStatus == status_idle) {
            if (offsetY > 0 && mOnRefreshListener != null && mOnRefreshListener.canPull()) {
                offsetView((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_down_pull;
                return true;
            } else if (offsetY < 0 && mOnLoadMoreListener != null && mOnLoadMoreListener.canPull()) {
                offsetView((int) offsetY);
                sendCancelEvent(ev);
                mStatus = status_up_pull;
                return true;
            }
            return false;
        }

        if (mStatus == status_down_pull) {
            if (mContentView.getTop() + offsetY < 0) {
                offsetY = 0 - mContentView.getTop();
            }
            offsetView((int) offsetY);
            if (mContentView.getTop() == 0) {
                sendDownEvent(ev);
                mStatus = status_idle;
            }
            return true;
        }

        if (mStatus == status_up_pull) {
            if (mContentView.getBottom() + offsetY > getMeasuredHeight()) {
                offsetY = getMeasuredHeight() - mContentView.getBottom();
            }
            offsetView((int) offsetY);
            if (mContentView.getBottom() == getMeasuredHeight()) {
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
                mHeaderUIHandler.onRefreshBegin();  // ((TextView) mHeaderView).setText("正在刷新");
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.refresh();
                        }
                    }
                }, mContentView.getTop(), mHeaderView.getMeasuredHeight());
            } else {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                    }
                }, mContentView.getTop(), 0);
            }
            return false;
        }

        if (mStatus == status_up_pull) {
            if (canLoadMore()) {
                mLoading = true;
                mFooterHandler.onRefreshBegin(); //((TextView) mFooterView).setText("正在加载");
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.loadMore();
                        }
                    }
                }, mContentView.getTop(), -mHeaderView.getMeasuredHeight());
            } else {
                viewAnim(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStatus = status_idle;
                    }
                }, mContentView.getTop(), 0);
            }
            return false;
        }

        return false;
    }

    /**
     * view动画
     *
     * @param endListener 结束监听
     * @param values      动画区间值
     */
    private void viewAnim(final OnClickListener endListener, int... values) {
        ValueAnimator animator = ObjectAnimator.ofInt(values);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int offset = value - mContentView.getTop();
                offsetView(offset);
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
     *
     * @return true可以 false不可以
     */
    private boolean canRefresh() {
        return mContentView.getTop() > mHeaderView.getMeasuredHeight();
    }

    /**
     * 是否能加载更多
     *
     * @return true可以 false不可以
     */
    private boolean canLoadMore() {
        return mContentView.getBottom() + mFooterView.getMeasuredHeight() < getMeasuredHeight();
    }

    /**
     * 发送取消事件
     *
     * @param ev event
     */
    private void sendCancelEvent(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    /**
     * 发送按下事件
     *
     * @param ev event
     */
    private void sendDownEvent(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(ev);
    }

    /**
     * 设置头部和底部视图
     *
     * @param header 头部视图
     * @param footer 底部视图
     */
    public void setHeaderAndFooterView(View header, View footer) {
        if (mHeaderView != null && header != null && header != mHeaderView) {
            removeView(mHeaderView);
        }
        mHeaderView = header;
        addView(header, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        if (mFooterView != null && footer != null && footer != mFooterView) {
            removeView(mFooterView);
        }
        mFooterView = footer;
        addView(footer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置头部和底部UI handler
     *
     * @param headerHandler 头部 handler
     * @param footerHandler 底部 Handler
     */
    public void setHeaderAndFooterUIHandler(PullUIHandler headerHandler, PullUIHandler footerHandler) {
        mHeaderUIHandler = headerHandler;
        mFooterHandler = footerHandler;
    }

    /**
     * 移动内容控件
     *
     * @param offset 偏移量
     */
    private void offsetView(int offset) {
        mContentView.offsetTopAndBottom(offset);
        mHeaderView.offsetTopAndBottom(offset);
        mFooterView.offsetTopAndBottom(offset);

        mTop = mContentView.getTop();

        if (mRefreshing || mLoading) {
            return;
        }

        if (canRefresh()) {
            mHeaderUIHandler.onRefreshPrepare();  // ((TextView) mHeaderView).setText("松手刷新");
        } else {
            mHeaderUIHandler.onNormal();  //((TextView) mHeaderView).setText("下拉刷新");
        }

        if (canLoadMore()) {
            mFooterHandler.onRefreshPrepare();  //((TextView) mFooterView).setText("松手加载");
        } else {
            mFooterHandler.onNormal();  //((TextView) mFooterView).setText("上拉加载");
        }
    }

    /**
     * 设置刷新监听
     *
     * @param listener 监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    /**
     * 设置加载更多监听
     *
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


    /**
     * 拉动刷新UI
     */
    public interface PullUIHandler {

//        void intiLayout(int l, int t, int r, int b);

        /**
         * 正常状态
         */
        void onNormal();

        /**
         * 准备刷新
         */
        void onRefreshPrepare();

        /**
         * 开始刷新
         */
        void onRefreshBegin();

        /**
         * 完成刷新
         */
        void onRefreshComplete();
    }
}
