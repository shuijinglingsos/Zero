package com.elf.zerodemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.LoopViewPager;

public class LoopViewPagerActivity extends AppBaseActivity {

    private LoopViewPager mLoopViewPager;
    private LoopAdapter mLoopAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_view_pager);

        mLoopViewPager = new LoopViewPager(this);  //LoopViewPager) findViewById(R.id.loopViewPager);
        mLoopAdapter = new LoopAdapter(this, 5);
        mLoopViewPager.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));
        mLoopViewPager.setAdapter(mLoopAdapter);
        mLoopViewPager.startAutoPlay();

        mListView = (ListView) findViewById(R.id.listView);
        mListView.addHeaderView(mLoopViewPager);
        mListView.setAdapter(new ListViewAdapter(this));

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            int preFirst=0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(preFirst!=0 && firstVisibleItem==0){
                    int i= mLoopViewPager.getViewPager().getCurrentItem();
                    mLoopViewPager.setAdapter(mLoopAdapter);
                    mLoopViewPager.getViewPager().setCurrentItem(i);
                }
                preFirst = firstVisibleItem;
            }
        });
    }

    public void onShowIndex(View view) {
        showToast("" + mLoopViewPager.getRealPosition());
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

    public static class ListViewAdapter extends ArrayAdapter<String> {

        public ListViewAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return 50;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView tv = new TextView(getContext());
            tv.setPadding(20, 20, 20, 20);
            tv.setText("" + position);
            return tv;
        }
    }


    @Override
    protected void onDestroy() {
        if (mLoopViewPager != null) {
            mLoopViewPager.stopAutoPlay();
        }
        super.onDestroy();

    }
}
