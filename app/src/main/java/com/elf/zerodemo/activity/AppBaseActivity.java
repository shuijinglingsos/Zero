package com.elf.zerodemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AppBaseActivity extends AppCompatActivity {

    public final static String ARG_TITLE = "arg_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(ARG_TITLE)) {
            setTitle(getIntent().getStringExtra(ARG_TITLE));
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
