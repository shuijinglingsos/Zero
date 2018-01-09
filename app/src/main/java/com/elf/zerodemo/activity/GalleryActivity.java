package com.elf.zerodemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elf.zerodemo.R;

public class GalleryActivity extends AppBaseActivity {

    private final static String ARG_PATH = "arg_path";
    private final static String ARG_POSITION = "arg_position";

    private TextView mTvPageNumber;

    private String[] mPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mTvPageNumber = (TextView) findViewById(R.id.tv_page_number);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        mPaths = getIntent().getStringArrayExtra(ARG_PATH);

        GalleryAdapter adapter = new GalleryAdapter(this, mPaths);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showPageNumber(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int position = getIntent().getIntExtra(ARG_POSITION, 0);
        showPageNumber(position);
        viewPager.setCurrentItem(position, false);
    }

    private void showPageNumber(int position) {
        if (mPaths == null) {
            mTvPageNumber.setVisibility(View.GONE);
        } else {
            mTvPageNumber.setVisibility(View.VISIBLE);
            mTvPageNumber.setText((position + 1) + "/" + mPaths.length);
        }
    }

    public static void open(Context context, String[] paths, int position) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(ARG_PATH, paths);
        intent.putExtra(ARG_POSITION, position);
        context.startActivity(intent);
    }

    public static class GalleryAdapter extends PagerAdapter {

        private Context mContext;
        private String[] mItems;

        public GalleryAdapter(Context context, String[] items) {
            mContext = context;
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems == null ? 0 : mItems.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            container.addView(imageView);

            Glide.with(mContext).load(mItems[position]).skipMemoryCache(true).into(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }
}
