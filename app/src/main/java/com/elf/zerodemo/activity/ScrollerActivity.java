package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.NestedScrollingLayout;
import com.elf.zerodemo.widget.NestedScrollingWebView;
import com.elf.zerodemo.widget.ScrollViewGroup;

public class ScrollerActivity extends AppBaseActivity {

    private NestedScrollingLayout mLinearLayoutScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);

        mLinearLayoutScroll = (NestedScrollingLayout)findViewById(R.id.root);

//        viewGroup()；
        webView();
    }

    private void viewGroup(){
        ScrollViewGroup scrollViewGroup = (ScrollViewGroup) findViewById(R.id.scrollView);
        for (int i = 0; i < 20; i++) {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setText("我是Item = " + (i + 1));
            tv.setTag("我是Item = " + (i + 1));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast(String.valueOf(v.getTag()));
                }
            });
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showToast(String.valueOf(v.getTag()) + " long");
                    return true;
                }
            });

            scrollViewGroup.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));

            View view = new View(this);
            view.setBackgroundResource(R.color.colorAccent);
            scrollViewGroup.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
        scrollViewGroup.mScrollFling=new ScrollViewGroup.ScrollFling() {
            @Override
            public void fling(float velocity) {
                mLinearLayoutScroll.fling(velocity);
            }
        };
    }

    private void webView(){
        NestedScrollingWebView scrollViewGroup = (NestedScrollingWebView) findViewById(R.id.scrollView);
        WebSettings settings = scrollViewGroup.getSettings();
        settings.setJavaScriptEnabled(true);
        scrollViewGroup.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        scrollViewGroup.loadUrl("http://m.163.com/apps");
        scrollViewGroup.mScrollFling=new NestedScrollingWebView.ScrollFling() {
            @Override
            public void fling(float velocity) {
                mLinearLayoutScroll.fling(velocity);
            }
        };
    }

}
