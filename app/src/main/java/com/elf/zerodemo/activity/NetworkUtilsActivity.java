package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.elf.zero.utils.NetworkUtils;
import com.elf.zerodemo.R;

public class NetworkUtilsActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_utils);

        StringBuilder sb = new StringBuilder();
        sb.append("是否有网络连接：").append(NetworkUtils.isNetworkConnection());
        sb.append("\n是否是wifi网络：").append(NetworkUtils.isWifiConnection());
        sb.append("\n是否是移动网络：").append(NetworkUtils.isMobileConnection());
        sb.append("\n当前网络类型：").append(NetworkUtils.getNetworkType());

        ((TextView) findViewById(R.id.textView)).setText(sb.toString());
    }
}
