package com.elf.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 9宫格
 * Created by Lidong on 2018/1/15.
 */
public class NineGridLayout extends ViewGroup {

    private final static String TAG = FlowLayout.class.getSimpleName();
    private final int mLineSpacing = 20;

    public NineGridLayout(Context context) {
        super(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();

        //1个项目
        if (childCount == 1) {
            View view = getChildAt(0);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            int childWidth = (widthSize - mPaddingLeft - mPaddingRight) / 3 * 2;
            setMeasuredDimension(widthSize, childWidth + mPaddingTop + mPaddingBottom);
            return;
        }

        //2和4个项目
        if (childCount == 2 || childCount == 4) {
            if (heightMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(widthSize, heightSize);
            } else {
                int childWidth = (getMeasuredWidth() - mPaddingLeft - mPaddingRight - mLineSpacing) / 2;
                int row = childCount / 2;
                int height = childWidth * row + mPaddingTop + mPaddingBottom + (mLineSpacing * (row - 1));
                setMeasuredDimension(widthSize, height);
            }
            return;
        }

        //3和4个以上
        if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize);
        } else {
            int childWidth = (getMeasuredWidth() - mPaddingLeft - mPaddingRight - mLineSpacing * 2) / 3;
            int row = childCount / 3;
            if (childCount % 3 > 0) {
                row++;
            }
            int height = childWidth * row + mPaddingTop + mPaddingBottom + (mLineSpacing * (row - 1));
            setMeasuredDimension(widthSize, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int childCount = getChildCount();
        if (childCount == 1) {
            int childWidth = (getMeasuredWidth() - mPaddingLeft - mPaddingRight)/3*2;
            View child = getChildAt(0);
            child.layout(mPaddingLeft, mPaddingTop,
                    mPaddingLeft + childWidth, mPaddingTop + childWidth);
            return;
        }

        if (childCount == 2 || childCount == 4) {
            int useWidth = mPaddingLeft, useHeight = mPaddingTop;
            int childWidth = (getMeasuredWidth() - mPaddingLeft - mPaddingRight - mLineSpacing) / 2;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (i > 0 && i % 2 == 0) {
                    useWidth = mPaddingLeft;
                    useHeight = useHeight + childWidth + mLineSpacing;
                }
                child.layout(useWidth, useHeight, useWidth + childWidth, useHeight + childWidth);
                useWidth = useWidth + childWidth + mLineSpacing;
            }
            return;
        }

        int useWidth = mPaddingLeft, useHeight = mPaddingTop;
        int childWidth = (getMeasuredWidth() - mPaddingLeft - mPaddingRight - mLineSpacing*2) / 3;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i > 0 && i % 3 == 0) {
                useWidth = mPaddingLeft;
                useHeight = useHeight + childWidth + mLineSpacing;
            }
            child.layout(useWidth, useHeight, useWidth + childWidth, useHeight + childWidth);
            useWidth = useWidth + childWidth + mLineSpacing;
        }
    }
}
