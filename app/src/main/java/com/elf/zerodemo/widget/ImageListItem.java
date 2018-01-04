package com.elf.zerodemo.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    }

    public void setData(ImageDetail data) {

        Glide.with(getContext()).load(data.path).centerCrop()
                .placeholder(R.mipmap.ic_launcher).into(mImageView);

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
