package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.NestedScrollingChildView;

public class NestedScrollingActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling);

//        NestedScrollingChildView childView = (NestedScrollingChildView) findViewById(R.id.NestedScrollingChildView);
//        childView.loadUrl("http://www.baidu.com");
//        showData(20);
    }

    public void showData(int count) {
        String[] names = new String[count];
        for (int i = 1; i <= names.length; i++) {
            names[i - 1] = i + "";
        }
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1);
        mArrayAdapter.addAll(names);
    }

    public void onClick(View view) {
    }
}
