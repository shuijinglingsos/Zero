package com.elf.zerodemo.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elf.zero.widget.AbsLinearLayout;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.ImageDetail;

import java.io.File;

/**
 * 图片列表项目
 * Created by Lidong on 2018/1/4.
 */
public class ImageListItem extends AbsLinearLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public ImageListItem(Context context) {
        super(context);
    }

    public ImageListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.widget_image_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mImageView = getViewById(R.id.imageView);
        mTextView = getViewById(R.id.textView);

        int width = (1080-12) / 3;

        setLayoutParams(new ViewGroup.LayoutParams(width, width));
    }

    public void setData(ImageDetail data) {

        if(data.name.endsWith("gif")) {
            mTextView.setText("动图");
            mTextView.setVisibility(VISIBLE);
            Glide.with(getContext()).load(data.path).asBitmap().centerCrop().skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mImageView);
        }else{
            Glide.with(getContext()).load(data.path).centerCrop().skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mImageView);
            mTextView.setVisibility(GONE);
        }

//        if(TextUtils.isEmpty(data.fmPaht)) {
//            Cursor cursor1 = getContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
//                    , new String[]{
//                            MediaStore.Images.Thumbnails.IMAGE_ID,
//                            MediaStore.Images.Thumbnails.DATA
//                    }
//                    , MediaStore.Images.Thumbnails.IMAGE_ID + "=?"
//                    , new String[]{data.id + ""}
//                    , null);
//
//            if (cursor1 != null && cursor1.moveToFirst()) {
//                data.fmPaht = cursor1.getString(1);
//            }
//        }
//
//        if(!TextUtils.isEmpty(data.fmPaht)) {
//            mImageView.setImageURI(Uri.fromFile(new File(data.fmPaht)));
//        }else{
//            mImageView.setImageResource(R.mipmap.ic_launcher);
//        }
    }
}
