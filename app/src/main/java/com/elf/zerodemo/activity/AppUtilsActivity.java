package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.elf.zero.utils.AppUtils;
import com.elf.zerodemo.R;

public class AppUtilsActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_utils);

        StringBuilder sb = new StringBuilder();
        sb.append("VersionName：").append(AppUtils.getVersionName());
        sb.append("\nVersionCode：").append(AppUtils.getVersionCode());
        sb.append("\nmeta：").append(AppUtils.getMetaValue("metaName"));

        ((TextView) findViewById(R.id.textView)).setText(sb.toString());
    }
}
