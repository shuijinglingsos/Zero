package com.elf.zerodemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码空间
 * Created by Lidong on 2018/1/31.
 */
public class GesturePasswordWidget extends ViewGroup {

    protected List<GesturePasswordPointView> mSelectedPointViews = new ArrayList<>();  //选中的点
    private Paint mPaint;  //画笔
    private Path mPath;   //路径
    private Point mLastPoint;  //最后点的位置
    private boolean mComplete;  //是否完成

    private GestureLockComplete onGestureLockComplete;  //完成回调

    public GesturePasswordWidget(Context context) {
        super(context);
        init();
    }

    public GesturePasswordWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);

        //路径
        mPath = new Path();

        //点控件
        for (int i = 0; i < 9; i++) {
            GesturePasswordPointView pv = new GesturePasswordPointView(getContext());
            pv.setPadding(60, 60, 60, 60);
            pv.row = i / 3;
            pv.col = i % 3;
            pv.index = i;
            this.addView(pv);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        //计算子控件的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(mWidth / 3, mHeight / 3);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置子控件的位置
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;
            int col = i % 3;
            View v = getChildAt(i);

            int left = col * getWidth() / 3;
            int top = row * getHeight() / 3;
            int right = left + getWidth() / 3;
            int bottom = top + getHeight() / 3;

            v.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mComplete) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                GesturePasswordPointView pv = getPointView(x, y);
                if (pv != null && !mSelectedPointViews.contains(pv)) {

                    if (mSelectedPointViews.size() > 0) {
                        GesturePasswordPointView temp = mSelectedPointViews.get(mSelectedPointViews.size() - 1);
                        int row = Math.abs(temp.row - pv.row);
                        int col = Math.abs(temp.col - pv.col);

                        if (row > 1 || col > 1) {

                            if (temp.row == pv.row || temp.col == pv.col || (row > 1 && col > 1)) {

                                row = row == 0 ? temp.row : row - 1;
                                col = col == 0 ? temp.col : col - 1;

                                int index = row * 3 + col;
                                GesturePasswordPointView middlePv = (GesturePasswordPointView) getChildAt(index);
                                if (!mSelectedPointViews.contains(middlePv)) {
                                    selectedPointView(middlePv);
                                }
                            }
                        }
                    }
                    selectedPointView(pv);
                }

                if (mLastPoint == null) {
                    mLastPoint = new Point(x, y);
                } else {
                    mLastPoint.set(x, y);
                }

                break;

            case MotionEvent.ACTION_UP:
                mLastPoint = null;
                mComplete = true;
                if (onGestureLockComplete != null) {
                    onGestureLockComplete.complete();
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 返回当前坐标所在的控件
     *
     * @param x x
     * @param y y
     * @return 控件
     */
    private GesturePasswordPointView getPointView(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            GesturePasswordPointView pv = (GesturePasswordPointView) getChildAt(i);
            if (x > pv.getLeft() + pv.getPaddingLeft() &&
                    x < pv.getRight() - pv.getPaddingRight() &&
                    y > pv.getTop() + pv.getPaddingTop() &&
                    y < pv.getBottom() - pv.getPaddingBottom()) {
                return pv;
            }
        }
        return null;
    }

    /**
     * 选中点控件
     *
     * @param pv 点控件
     */
    protected void selectedPointView(GesturePasswordPointView pv) {
        pv.setSelected(true);
        mSelectedPointViews.add(pv);

        int mLastPathX = pv.getLeft() / 2 + pv.getRight() / 2;
        int mLastPathY = pv.getTop() / 2 + pv.getBottom() / 2;
        if (mSelectedPointViews.size() == 1) {
            mPath.moveTo(mLastPathX, mLastPathY);
        } else {
            mPath.lineTo(mLastPathX, mLastPathY);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }

        //绘制指引线
        if (mSelectedPointViews.size() > 0) {

            GesturePasswordPointView pv = mSelectedPointViews.get(mSelectedPointViews.size() - 1);

            int mLastPathX = pv.getLeft() / 2 + pv.getRight() / 2;
            int mLastPathY = pv.getTop() / 2 + pv.getBottom() / 2;

            if (mLastPoint != null)
                canvas.drawLine(mLastPathX, mLastPathY, mLastPoint.x,
                        mLastPoint.y, mPaint);
        }
    }

    /**
     * 重置
     */
    public void reset() {
        for (int i = 0; i < getChildCount(); i++) {
            GesturePasswordPointView pv = (GesturePasswordPointView) getChildAt(i);
            pv.setSelected(false);
        }
        mSelectedPointViews.clear();
        mPath.reset();
        mComplete = false;
        invalidate();
    }

    /**
     * 获取被选中的点控件
     */
    public List<GesturePasswordPointView> getSelectedPointViews() {
        return mSelectedPointViews;
    }

    /**
     * 设置完成回调状态
     */
    public void setOnGestureLockComplete(GestureLockComplete listener) {
        onGestureLockComplete = listener;
    }

    /**
     * 是否完成
     *
     * @return 完成返回true，否则返回false
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * 手势锁完成回调
     */
    public interface GestureLockComplete {
        void complete();
    }
}
