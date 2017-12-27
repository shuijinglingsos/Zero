package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elf.zero.net.DefaultNetRequest;
import com.elf.zero.net.KeyValuePair;
import com.elf.zero.net.NetDownloadListener;
import com.elf.zero.net.NetException;
import com.elf.zero.net.NetRequest;
import com.elf.zero.net.NetRequestListener;
import com.elf.zero.net.NetResponse;
import com.elf.zerodemo.R;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 网络连接测试页面
 */
public class NetRequestActivity extends AppBaseActivity {

    private TextView mTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_connection);

        mTextView = (TextView) findViewById(R.id.textView);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setText("https://www.baidu.com");
    }

    private void showResult(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(msg);
            }
        });
    }

    private String getUrl() {
        return mEditText.getText().toString().trim();
    }

    public void onGet(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    NetRequest request = new DefaultNetRequest();
                    request.setUrl(getUrl());
                    NetResponse response = request.get(null);
                    String result = new String(response.getResponseContent());
                    showResult(result);
                } catch (NetException e) {
                    e.printStackTrace();
                    showResult(e.getResponseCode() + " " + e.getMessage());
                }
            }
        }.start();
    }

    public void onGetAsyn(View view) {
        NetRequest request = new DefaultNetRequest();
        request.setUrl(getUrl());
        request.get(null, new NetRequestListener() {
            @Override
            public void onSuccess(NetResponse response) {
                String result = new String(response.getResponseContent());
                showResult(result);
            }

            @Override
            public void onFailure(NetException exception) {
                showResult(exception.getResponseCode() + " " + exception.getMessage());
            }
        });
    }

    public void onPost(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    NetRequest request = new DefaultNetRequest();
                    request.setUrl(getUrl());
                    NetResponse response = request.post(null);
                    String result = new String(response.getResponseContent());
                    showResult(result);
                } catch (NetException e) {
                    e.printStackTrace();
                    showResult(e.getResponseCode() + " " + e.getMessage());
                }
            }
        }.start();
    }

    public void onPostAsyn(View view) {
        NetRequest request = new DefaultNetRequest();
        request.setUrl(getUrl());
        request.post(null, new NetRequestListener() {
            @Override
            public void onSuccess(NetResponse response) {
                String result = new String(response.getResponseContent());
                showResult(result);
            }

            @Override
            public void onFailure(NetException exception) {
                showResult(exception.getResponseCode() + " " + exception.getMessage());
            }
        });
    }

    public void onDownload(View view) {
        File file = new File(getCacheDir().getPath() + "/file.apk");

        String url = "http://192.168.1.110/app/cmb_app.apk";

        NetRequest request = new DefaultNetRequest();
        request.setUrl(url);
        request.download(file, new NetDownloadListener() {
            @Override
            public void onProgress(int readCount, int totalCount) {
                showResult(readCount + "/" + totalCount);
            }

            @Override
            public void onFailure(NetException exception) {
                showResult(exception.getResponseCode() + " " + exception.getMessage());
            }

            @Override
            public void onSuccess(File file, List<KeyValuePair<String>> headers) {
                StringBuilder sb = new StringBuilder();
                for (KeyValuePair<String> loop : headers) {
                    sb.append("\n").append(loop.getKey()).append("=").append(loop.getValue());
                }

                showResult("success：" + file.getPath() + sb.toString());
            }
        });
    }
}
