package com.elf.zero.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * dialog基类
 * Created by Lidong on 2017/12/5.
 */
public abstract class AbsAlertDialog extends AlertDialog {

    protected AbsAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected AbsAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(getLayoutResId());
        initView();
    }

    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    /**
     * 获取资源id
     *
     * @return 资源id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化布局
     */
    protected abstract void initView();
}
