package com.elf.zerodemo.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.elf.zero.utils.LogUtils;
import com.elf.zerodemo.R;

/**
 * Behavior
 * Created by Lidong on 2018/1/25.
 */
public class DependentFABBehavior  extends CoordinatorLayout.Behavior {

    private final static String TAG = DependentFABBehavior.class.getSimpleName();

    public DependentFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        LogUtils.v(TAG, "--layoutDependsOn:" + dependency.toString());
        return dependency.getId() == R.id.nestedScrollView;
//            return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        LogUtils.v(TAG, "--dependency:" + dependency.toString());
        LogUtils.v(TAG, "--child:" + child.toString());

        child.layout(0,dependency.getTop()-child.getMeasuredHeight(),child.getMeasuredWidth(),
                dependency.getTop());

//        if (dependency.getVisibility() == View.GONE) {
//            child.setTranslationY(-dependency.getMeasuredHeight());
//        } else {
//            child.setTranslationY(dependency.getMeasuredHeight());
//        }
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        LogUtils.v(TAG,"---onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        LogUtils.v(TAG,"---onNestedScroll:dyUnconsumed="+dyUnconsumed);
        if (dyUnconsumed < 0) {
            coordinatorLayout.scrollBy(0, dyUnconsumed);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        LogUtils.v(TAG,"---onNestedPreFling");
        return false;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        LogUtils.v(TAG,"---onNestedFling:"+consumed);
        return true;
    }
}
