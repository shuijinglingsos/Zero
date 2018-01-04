package com.elf.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * LinearLayout根布局自定义控件基类
 * Created by Lidong on 2016/2/1.
 */
public abstract class AbsLinearLayout extends LinearLayout {

    public AbsLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public AbsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * 初始化布局
     */
    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), this);
        initView(attrs);
    }

    /**
     * 获取指定资源Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 布局资源ID
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化view
     */
    protected abstract void initView(AttributeSet attrs);

    /**
     * 显示toast
     */
    protected void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
