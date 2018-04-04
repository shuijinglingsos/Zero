package com.elf.zerodemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.elf.zerodemo.R;

public class NewsDetailActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        final WebView webView = new WebView(this);
        webView.loadUrl("https://www.ithome.com");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(new NewsDetailAdapter(NewsDetailActivity.this, webView));
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private static class NewsDetailAdapter extends BaseAdapter {

        private Context mContext;
        private WebView mWebView;

        public NewsDetailAdapter(Context context,WebView webView) {
            mContext = context;
            mWebView = webView;
        }

        @Override
        public int getCount() {
            return 35;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            switch (getItemViewType(position)) {
                case 0:
                    if (convertView == null) {
                        return mWebView;
                    }
                    break;

                case 1:
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
                    }
                    if(position==5){
                        convertView.setVisibility(View.GONE);
                    }
                    ((TextView) convertView).setText("评论内容" + position);
                    break;
            }

            return convertView;
        }
    }
}
