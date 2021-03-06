package com.elf.zero.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elf.zero.app.BaseLinearLayout;
import com.elf.zero.utils.DateTimeUtils;
import com.elf.zero.utils.LogUtils;

/**
 * 图片列表项目
 * Created by Lidong on 2018/1/4.
 */
public class AlbumFileListItem extends BaseLinearLayout {

    private ImageView mImageView, mIvCheck, mIvVideo;
    private TextView mTextView;
    private View mViewCheckShade;

    private AlbumFile mAlbumFile;

    public AlbumFileListItem(Context context) {
        super(context);
    }

    public AlbumFileListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.zero_image_widget_file_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mImageView = getViewById(R.id.imageView);
        mIvVideo = getViewById(R.id.iv_video);
        mTextView = getViewById(R.id.textView);
        mIvCheck = getViewById(R.id.iv_check);
        mViewCheckShade = getViewById(R.id.view_check_shade);
        mIvCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlbumFile.checked = !mAlbumFile.checked;
                if (mAlbumFile.checked) {
                    mIvCheck.setImageResource(android.R.drawable.checkbox_on_background);
                } else {
                    mIvCheck.setImageResource(android.R.drawable.checkbox_off_background);
                }
            }
        });
    }

    public void setData(AlbumFile data) {
        if (mAlbumFile == data) {
            return;
        }

        mAlbumFile = data;
        LogUtils.v("Album", "mimeType : " + data.mimeType);
        if (AlbumFile.TYPE_IMAGE == mAlbumFile.fileType) {
            mIvVideo.setVisibility(GONE);
            if (data.mimeType.contains("gif")) {
                mTextView.setText("动图");
                mTextView.setVisibility(VISIBLE);
                Glide.with(getContext()).load(data.path).asBitmap().centerCrop().dontAnimate().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mImageView);
            } else {
                Glide.with(getContext()).load(data.path).centerCrop().dontAnimate().skipMemoryCache(true)
                        .into(mImageView);
                mTextView.setVisibility(GONE);
            }
        } else if (AlbumFile.TYPE_VIDEO == mAlbumFile.fileType) {
            mIvVideo.setVisibility(VISIBLE);
            mTextView.setText(DateTimeUtils.toMinute(data.duration));
            mTextView.setVisibility(VISIBLE);
            Glide.with(getContext()).load(data.path).centerCrop().dontAnimate().skipMemoryCache(true)
                    .into(mImageView);
        }
    }

    public void setChecked(boolean checked) {
        if (checked) {
            mIvCheck.setImageResource(android.R.drawable.checkbox_on_background);
            mViewCheckShade.setVisibility(VISIBLE);
        } else {
            mIvCheck.setImageResource(android.R.drawable.checkbox_off_background);
            mViewCheckShade.setVisibility(GONE);
        }
    }

    public void setOnCheckedListener(OnClickListener listener){
        mIvCheck.setOnClickListener(listener);
    }
}
