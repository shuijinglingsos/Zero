package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.elf.zero.widget.AbsLinearLayout;
import com.elf.zerodemo.R;

/**
 * 记载更多控件
 * Created by Lidong on 2018/1/16.
 */
public class LoadMoreWidget extends AbsLinearLayout {

    public LoadMoreWidget(Context context) {
        super(context);
    }

    public LoadMoreWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.widget_load_more;
    }

    @Override
    protected void initView(AttributeSet attrs) {

    }
}
