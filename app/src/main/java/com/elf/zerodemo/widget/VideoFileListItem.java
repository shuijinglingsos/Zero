package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elf.zero.widget.AbsLinearLayout;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;

/**
 * 视屏列表项目
 * Created by Lidong on 2018/1/5.
 */
public class VideoFileListItem extends AbsLinearLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public VideoFileListItem(Context context) {
        super(context);
    }

    public VideoFileListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.widget_album_video_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mImageView = getViewById(R.id.imageView);
        mTextView = getViewById(R.id.textView);
    }

    public void setData(AlbumFile data) {
        mTextView.setText("视频 - " + data.duration);
        Glide.with(getContext()).load(data.path).centerCrop().skipMemoryCache(true)
                .into(mImageView);
    }
}
