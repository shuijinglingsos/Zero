package com.elf.zero.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elf.zero.R;
import com.elf.zero.activity.BaseActivity;

import java.util.List;

public class GalleryActivity extends BaseActivity {

    private final static String ARG_POSITION = "arg_position";

    private static List<AlbumFile> mAlbumFiles;
    private static List<AlbumFile> mSelected;
    private static int mMaxSelected = 0;

    private ViewPager mViewPager;
    private TextView mTvPageNumber,mTvSelectedNumber;
    private ImageView mIvCheck;
    private LinearLayout mLlSelected;

    private GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_album_activity_gallery);
        transparentStatus();
        initView();
        int position = getIntent().getIntExtra(ARG_POSITION, 0);

        mGalleryAdapter = new GalleryAdapter(this.getSupportFragmentManager(), mAlbumFiles);
        mViewPager.setAdapter(mGalleryAdapter);

        showPageNumber(position);
        showSelectedNumber();
        mViewPager.setCurrentItem(position, false);
    }

    private void initView() {
        mLlSelected = (LinearLayout) findViewById(R.id.ll_selected);
        mTvSelectedNumber = (TextView) findViewById(R.id.tv_select_number);
        mIvCheck = (ImageView) findViewById(R.id.iv_check);
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

        mLlSelected.setOnClickListener(new View.OnClickListener() {
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
                showSelectedNumber();

                mGalleryAdapter.notifyDataSetChanged();
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

    private void showSelectedNumber(){
        mTvSelectedNumber.setText(String.valueOf(mSelected.size()));
    }

    @Override
    protected void onDestroy() {
        mAlbumFiles = null;
        mSelected = null;
        mMaxSelected = 0;
        super.onDestroy();
    }

    private void transparentStatus(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
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
