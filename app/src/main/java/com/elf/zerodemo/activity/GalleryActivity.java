package com.elf.zerodemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elf.zerodemo.R;
import com.elf.zerodemo.adapter.GalleryAdapter;
import com.elf.zerodemo.model.AlbumFile;

import java.util.List;

public class GalleryActivity extends AppBaseActivity {

    private final static String ARG_POSITION = "arg_position";

    private static List<AlbumFile> mAlbumFiles;
    private static List<AlbumFile> mSelected;
    private static int mMaxSelected = 0;

    private ViewPager mViewPager;
    private TextView mTvPageNumber;
    private ImageView mIvCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView();
        int position = getIntent().getIntExtra(ARG_POSITION, 0);

        GalleryAdapter adapter = new GalleryAdapter(this.getSupportFragmentManager(), mAlbumFiles);
        mViewPager.setAdapter(adapter);

        showPageNumber(position);
        mViewPager.setCurrentItem(position, false);
    }

    private void initView() {
        mTvPageNumber = (TextView) findViewById(R.id.tv_page_number);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        mIvCheck = (ImageView) findViewById(R.id.iv_check);
        mIvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewPager.getCurrentItem();
                AlbumFile albumFile = mAlbumFiles.get(position);

                if (albumFile.checked) {
                    albumFile.checked = false;
                    mSelected.remove(albumFile);
                    mIvCheck.setImageResource(android.R.drawable.checkbox_off_background);
                } else {
                    if (mSelected.size() < mMaxSelected) {
                        albumFile.checked = true;
                        mSelected.add(albumFile);
                        mIvCheck.setImageResource(android.R.drawable.checkbox_on_background);
                    } else {
                        showToast("最多选中 " + mMaxSelected + " 项");
                    }
                }
            }
        });
    }

    private void showPageNumber(int position) {
        if (mAlbumFiles == null) {
            mTvPageNumber.setVisibility(View.GONE);
        } else {
            mTvPageNumber.setVisibility(View.VISIBLE);
            mTvPageNumber.setText((position + 1) + "/" + mAlbumFiles.size());
            if (mAlbumFiles.get(position).checked) {
                mIvCheck.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                mIvCheck.setImageResource(android.R.drawable.checkbox_off_background);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mAlbumFiles = null;
        mSelected = null;
        mMaxSelected = 0;
        super.onDestroy();
    }

    public static void open(Activity context, List<AlbumFile> paths, List<AlbumFile> selected,
                            int maxSelected, int position, int requestCode) {

        mAlbumFiles = paths;
        mSelected = selected;
        mMaxSelected = maxSelected;

        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(ARG_POSITION, position);
        context.startActivityForResult(intent, requestCode);
    }
}
