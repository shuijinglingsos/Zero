package com.elf.zerodemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 手势密码 点空间
 * Created by Lidong on 2018/1/31.
 */
public class GesturePasswordPointView extends View {

    private int mCenterX, mCenterY;  //中心点

    private int mExCircleRadius; //外圆半径
    private int mExCircleColor = 0xFF0000FF;  //外圆颜色

    private int mInCircleRadius;  //内圆半径
    private int mInCircleColor = 0xFFFF0000;  //内圆颜色

    private boolean mSelected; //是否选中状态

    public int row, col, index;  //行数，列数，索引

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  //画笔

    public GesturePasswordPointView(Context context) {
        super(context);
    }

    public GesturePasswordPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        cal(mWidth, mHeight);
    }

    /**
     * 计算画图用到的数据
     */
    protected void cal(int width, int height) {

        //外圆直径  宽高哪个值小取哪个值
        int diameters;
        if (width < height) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            diameters = width - paddingLeft - paddingRight;
        } else {
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            diameters = height - paddingTop - paddingBottom;
        }

        mExCircleRadius = diameters / 2;
        mInCircleRadius = mExCircleRadius / 3;

        mCenterX = width / 2;
        mCenterY = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制外圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mExCircleColor);
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(mCenterX, mCenterY, mExCircleRadius, mPaint);

        //如果被选中则绘制内圆
        if (mSelected) {
            // 绘制内圆
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mInCircleColor);
            canvas.drawCircle(mCenterX, mCenterY, mInCircleRadius, mPaint);
        }
    }

    /**
     * 是否已选中
     *
     * @return true中， false未选中
     */
    public boolean isSelected() {
        return mSelected;
    }

    /**
     * 设置选中状态
     *
     * @param mSelected true选中，false未选中
     */
    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
        invalidate();
    }

}
