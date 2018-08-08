package com.elf.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elf.zero.app.BaseLinearLayout;
import com.elf.zero.app.R;


/**
 * 默认刷新
 * Created by Lidong on 2018/1/18.
 */
public class DefaultPullRefreshHeader extends BaseLinearLayout implements PullRefreshLayout.PullUIHandler {

    private ProgressBar mProgressBar;
    private TextView mTextView;

    public DefaultPullRefreshHeader(Context context) {
        super(context);
    }

    public DefaultPullRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.zero_widget_default_pull_refresh_header;
    }



    @Override
    protected void initView(AttributeSet attrs) {
        mProgressBar = getViewById(R.id.progressBar);
        mTextView = getViewById(R.id.textView);
    }

//    @Override
//    public void intiLayout(int l, int t, int r, int b) {
//        layout(l, t, r, b);
//    }

    @Override
    public void onNormal() {
        mProgressBar.setVisibility(INVISIBLE);
        mTextView.setText("下拉刷新");
    }

    @Override
    public void onRefreshPrepare() {
        mProgressBar.setVisibility(INVISIBLE);
        mTextView.setText("松手刷新");
    }

    @Override
    public void onRefreshBegin() {
        mProgressBar.setVisibility(VISIBLE);
        mTextView.setText("正在刷新");
    }

    @Override
    public void onRefreshComplete() {
        mProgressBar.setVisibility(INVISIBLE);
        mTextView.setText("完成刷新");
    }
}
