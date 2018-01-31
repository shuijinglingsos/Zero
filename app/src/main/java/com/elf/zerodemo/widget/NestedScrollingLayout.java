package com.elf.zerodemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * Created by Lidong on 2018/1/26.
 */
public class NestedScrollingLayout extends ViewGroup {

    private OverScroller mOverScroller;

    public NestedScrollingLayout(Context context) {
        super(context);
        init();
    }

    public NestedScrollingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
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

    private void init(){
        mOverScroller = new OverScroller(getContext());
    }


    public void fling(float velocity) {
        mOverScroller.fling(getScrollX(), getScrollY(), 0, (int) velocity,
                0, 0, 0, getMeasuredHeight()/2);
        postInvalidate();
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mOverScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());

//            LogUtils.v(TAG, "---CurrVelocity" + mOverScroller.getCurrVelocity());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
    }
}
