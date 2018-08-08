package com.elf.zero.app;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * LinearLayout根布局自定义控件基类
 * Created by Lidong on 2016/2/1.
 */
public abstract class BaseLinearLayout extends LinearLayout {

    public BaseLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
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

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showDialog(String msg){
        new AlertDialog.Builder(getContext()).setMessage(msg).show();
    }
}
