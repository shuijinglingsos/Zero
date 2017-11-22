package com.elf.zerodemo.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.LoopViewPager;

public class LoopViewPagerActivity extends AppCompatActivity {

    private LoopViewPager mLoopViewPager;
    private LoopAdapter mLoopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_view_pager);

        mLoopViewPager = (LoopViewPager) findViewById(R.id.loopViewPager);
        mLoopAdapter = new LoopAdapter(this, 2);
        mLoopViewPager.setAdapter(mLoopAdapter);
    }

    public void onShowIndex(View view) {
        showToast("" + mLoopViewPager.getRealPosition(mLoopViewPager.getViewPager().getCurrentItem()));
    }

    public void onAutoPlay(View view) {
        mLoopViewPager.startAutoPlay();
    }

    public void onStopPlay(View view) {
        mLoopViewPager.stopAutoPlay();
    }

    public void onAddView(View view) {
        mLoopAdapter = new LoopAdapter(this, 5);
        mLoopViewPager.setAdapter(mLoopAdapter);
    }

    private static class LoopAdapter extends LoopViewPager.LoopViewPagerAdapter {

        private int mRealCount;

        public LoopAdapter(Context context, int realCount) {
            super(context);
            mRealCount = realCount;
        }

        @Override
        public View instantiateItemView(int position) {

            TextView tv = new TextView(mContext);
            tv.setText("" + position);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(30);

            if (position == 0) {
                tv.setBackgroundResource(R.drawable.timg);
            }
            if (position == 1) {
                tv.setBackgroundResource(R.drawable.timg_1);
            }
            if (position == 2) {
                tv.setBackgroundResource(R.drawable.timg_2);
            }
            if (position == 3) {
                tv.setBackgroundResource(R.drawable.timg);
            }
            if (position == 4) {
                tv.setBackgroundResource(R.drawable.timg_1);
            }
            return tv;
        }

        @Override
        public int getRealCount() {
            return mRealCount;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
