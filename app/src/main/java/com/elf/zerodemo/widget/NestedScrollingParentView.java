package com.elf.zerodemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elf.zero.utils.LogUtils;

/**
 * 嵌套滑动父类
 * Created by Lidong on 2018/1/24.
 */
public class NestedScrollingParentView extends ViewGroup implements NestedScrollingParent{

    private final static String TAG=NestedScrollingParentView.class.getSimpleName();

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    public NestedScrollingParentView(Context context) {
        super(context);
        init();
    }

    public NestedScrollingParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, top, getMeasuredWidth(), top + getChildAt(i).getMeasuredHeight());
            top += getChildAt(i).getMeasuredHeight();
        }
    }

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    //后于view滚动前
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        scrollBy(0, dyUnconsumed);//滚动
    }

    //先于view前滚动
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

//        LogUtils.v(TAG,"--dy:"+getScrollY());
//        LogUtils.v(TAG,"--getScrollY:"+getScrollY());
//
//        scrollBy(0, dy/2);//滚动
//        consumed[1] = dy/2;//告诉child我消费了多少

//        if (dy < 0) {  //向上滑
//            if (getScrollY() >= -(getChildAt(0).getMeasuredHeight()-40)) {
//                scrollBy(0, dy);//滚动
//                consumed[1] = dy;//告诉child我消费了多少
//            }
//        }else{  //向下滑
//            if(getScrollY()<0){
//                scrollBy(0, dy);//滚动
//                consumed[1] = dy;//告诉child我消费了多少
//            }
//        }
    }

    //先于Fling
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        LogUtils.v(TAG,"--onNestedFling:"+velocityY+",");
        return false;
    }

    //后于Fling
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        LogUtils.v(TAG,"--onNestedFling:"+velocityY+","+consumed);
        if(consumed){
            scrollBy(0,100);
        }

        return false;
    }


    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }
}
