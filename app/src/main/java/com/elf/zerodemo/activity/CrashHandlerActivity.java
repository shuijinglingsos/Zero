package com.elf.zerodemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.elf.zerodemo.R;

public class CrashHandlerActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_handler);
    }

    public void onClick(View view) {
        String asdfa = null;

        boolean result = asdfa.equals("adsf");
    }
}
