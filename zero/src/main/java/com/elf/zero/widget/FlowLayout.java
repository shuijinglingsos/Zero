package com.elf.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.elf.zero.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by Lidong on 2017/12/7.
 */
public class FlowLayout extends ViewGroup {

    private final static String TAG=FlowLayout.class.getSimpleName();
    private final int lineSpacing=20;

    private List<FlowRow> mFlowRows = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
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

        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineY = mPaddingTop;
        int lineHeight = 0;

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, lineY);
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int spaceWidth = mlp.leftMargin + childWidth + mlp.rightMargin;
            int spaceHeight = mlp.topMargin + childHeight + mlp.bottomMargin;
            if (lineUsed + spaceWidth > widthSize) {
                //approach the limit of width and move to next line
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineHeight = 0;
            }
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
        }
        setMeasuredDimension(
                widthSize,
                heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + mPaddingBottom
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int lineX = mPaddingLeft;
        int lineY = mPaddingTop;
        int lineWidth = r - l;
//        usefulWidth = lineWidth - mPaddingLeft - mPaddingRight;
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineHeight = 0;
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int spaceWidth = mlp.leftMargin + childWidth + mlp.rightMargin;
            int spaceHeight = mlp.topMargin + childHeight + mlp.bottomMargin;
            if (lineUsed + spaceWidth > lineWidth) {
                //approach the limit of width and move to next line
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineX = mPaddingLeft;
                lineHeight = 0;
            }
            child.layout(lineX + mlp.leftMargin, lineY + mlp.topMargin, lineX + mlp.leftMargin + childWidth, lineY + mlp.topMargin + childHeight);
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
            lineX += spaceWidth;

        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private class FlowRow {
        public List<View> flowItem = new ArrayList<>();

        public int getWidth() {
            int max = 0;
            for (View view : flowItem) {
                max += view.getMeasuredWidth();
            }
            return max;
        }

        public int getHeight() {
            int max = 0;
            for (View view : flowItem) {
                if (max < view.getMeasuredHeight()) {
                    max = view.getMeasuredHeight();
                }
            }
            return max;
        }
    }
}
