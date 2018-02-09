package com.elf.zerodemo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ViewAnimator;

import com.elf.zerodemo.R;

public class CoordinatorLayoutActivity extends AppBaseActivity {

    private final static String TAG = CoordinatorLayoutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

//        NestedScrollView view=(NestedScrollView)findViewById(R.id.nestedScrollView);
//        view.getMaxScrollAmount()
    }

    public void onClick(View view) {
        View v = findViewById(R.id.view);
//        v.setTranslationY(v.getTranslationY()+5);

        ObjectAnimator.ofFloat(v,"translationY",1,200,1).start();

//        if (v.getVisibility() == View.GONE) {
//            v.setVisibility(View.VISIBLE);
//        } else {
//            v.setVisibility(View.GONE);
//        }
    }


}
