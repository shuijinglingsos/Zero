package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elf.zero.image.ImageChoose;
import com.elf.zerodemo.R;

public class ImageChooseTestActivity extends AppBaseActivity {

    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_choose_test);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mTextView = (TextView) findViewById(R.id.tv_file);
    }

    private ImageChoose.OnChooseListener listener = new ImageChoose.OnChooseListener() {
        @Override
        public void choose(String[] files) {
            if (files != null && files.length > 0) {
                mTextView.setText(files[0]);
                Glide.with(ImageChooseTestActivity.this).load(files[0]).into(mImageView);
            } else {
                mTextView.setText("没有数据");
            }
        }

        @Override
        public void cancel() {
            mTextView.setText("已取消选择");
        }
    };

    public void onChoose(View view) {
        ImageChoose.get(this).listener(listener).open();
    }

    public void onAlbum(View view) {
        ImageChoose.get(this).listener(listener).openAlbum();
    }

    public void onCamera(View view) {
        ImageChoose.get(this).listener(listener).openCamera();
    }

    public void onChooseCrop(View view) {
        ImageChoose.get(this).crop().listener(listener).open();
    }

    public void onAlbumCrop(View view) {
        ImageChoose.get(this).cropAspect(1, 1).listener(listener).openAlbum();
    }

    public void onCameraCrop(View view) {
        ImageChoose.get(this).cropOutput(400, 400).listener(listener).openCamera();
    }
}
