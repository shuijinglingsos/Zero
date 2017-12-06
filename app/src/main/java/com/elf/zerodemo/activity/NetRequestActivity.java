package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.elf.zero.net.DefaultNetRequest;
import com.elf.zero.net.NetException;
import com.elf.zero.net.NetRequestListener;
import com.elf.zero.net.NetResponse;
import com.elf.zerodemo.R;

/**
 * 网络连接测试页面
 */
public class NetRequestActivity extends AppBaseActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_connection);

        mTextView = (TextView) findViewById(R.id.textView);
    }

    public void onTestGet(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    DefaultNetRequest request = new DefaultNetRequest();
                    request.setUrl("http://www.baidu.com");
                    NetResponse response = request.get();
                    if (response.getResponseCode() == 200) {
                        String result = new String(response.getResponseContent());
                        showResult(result);
                    } else {
                        showResult(response.getResponseCode() + " " + response.getResponseMessage());
                    }
                } catch (NetException e) {
                    e.printStackTrace();
                    showResult(e.getException().getMessage());
                }
            }
        }.start();
    }

    public void onTestGetAsyn(View view) {
        DefaultNetRequest request = new DefaultNetRequest();
        request.setUrl("http://www.baidu.com");
        request.get(new NetRequestListener() {
            @Override
            public void onSuccess(NetResponse response) {
                String result = new String(response.getResponseContent());
                showResult(result);
            }

            @Override
            public void onFailure(NetException exception) {
                showResult(exception.getResponseCode() + " " + exception.getException().getMessage());
            }
        });
    }

    private void showResult(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(msg);
            }
        });
    }

}
