package com.elf.zero.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * activity 基类
 * Created by Lidong on 2017/11/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private final static String TAG = BaseActivity.class.getSimpleName();


    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
