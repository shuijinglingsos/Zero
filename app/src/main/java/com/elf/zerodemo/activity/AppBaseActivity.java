package com.elf.zerodemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AppBaseActivity extends AppCompatActivity {

    public final static String ARG_TITLE = "arg_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getIntent().getStringExtra(ARG_TITLE));
    }
}
