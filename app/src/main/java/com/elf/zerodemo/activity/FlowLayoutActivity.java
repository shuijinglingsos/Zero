package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.elf.zero.widget.FlowLayout;
import com.elf.zerodemo.R;

public class FlowLayoutActivity extends AppBaseActivity {

    private ScrollView mScrollView;
    private FlowLayout mFlowLayout;
    private static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        mFlowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        i = 0;
    }

    public void onAddView(View view) {
        final Button button = new Button(this);
        button.setText("Button " + i);
        addViewToFlowLayout(button);
    }

    public void onAddViewLong(View view) {
        final Button button = new Button(this);
        button.setText("ButtonButtonButtonButtonButtonButtonButtonButton " + i);
        addViewToFlowLayout(button);
    }

    private void addViewToFlowLayout(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlowLayout.removeView(v);
            }
        });
        mFlowLayout.addView(view);
        mFlowLayout.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        i++;
    }
}
