package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elf.zero.utils.DeviceUtils;
import com.elf.zero.widget.AbsLinearLayout;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;

/**
 * 图片列表项目
 * Created by Lidong on 2018/1/4.
 */
public class ImageFileListItem extends AbsLinearLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public ImageFileListItem(Context context) {
        super(context);
    }

    public ImageFileListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.widget_album_image_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mImageView = getViewById(R.id.imageView);
        mTextView = getViewById(R.id.textView);
    }

    public void setData(AlbumFile data) {
        if(data.name.endsWith("gif")) {
            mTextView.setText("动图");
            mTextView.setVisibility(VISIBLE);
            Glide.with(getContext()).load(data.path).asBitmap().centerCrop().skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mImageView);
        }else{
            Glide.with(getContext()).load(data.path).centerCrop().skipMemoryCache(true)
                    .into(mImageView);
            mTextView.setVisibility(GONE);
        }
    }
}
