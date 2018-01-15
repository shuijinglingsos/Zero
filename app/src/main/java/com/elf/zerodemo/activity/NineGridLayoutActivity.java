package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;

import com.elf.zero.widget.NineGridLayout;
import com.elf.zerodemo.R;

public class NineGridLayoutActivity extends AppBaseActivity {

    private NineGridLayout mNineGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_grid_layout);

        mNineGridLayout = (NineGridLayout) findViewById(R.id.nineGridLayout);
    }

    public void onAddView(View view) {
        View item = new View(this);
        item.setBackgroundResource(R.color.colorAccent);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNineGridLayout.removeView(v);
            }
        });
        mNineGridLayout.addView(item);
    }
}
