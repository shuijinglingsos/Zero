package com.elf.zerodemo.fragment;

import android.animation.ArgbEvaluator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.elf.zero.utils.DeviceUtils;
import com.elf.zero.utils.LogUtils;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;

import java.io.File;

/**
 *
 * Created by Lidong on 2018/2/9.
 */
public class GalleryFragment extends Fragment {

    private final static String TAG = GalleryFragment.class.getSimpleName();

    private final static String ARG_ALBUM_FILE = "arg_album_file";

    private final static float mMaxScale = 3;
    private final static float mDoubleClickScale = 2;
    private final static int mZoomDuration = 250;

    private AlbumFile mAlbumFile;
    private SubsamplingScaleImageView mImageView;

    public static Fragment get(AlbumFile albumFile) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ALBUM_FILE, albumFile);

        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mAlbumFile = (AlbumFile) args.getSerializable(ARG_ALBUM_FILE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mImageView = view.findViewById(R.id.iv_scale);
        mImageView.setDoubleTapZoomDuration(mZoomDuration);
        mImageView.setDoubleTapZoomScale(mDoubleClickScale);
        mImageView.setMaxScale(mMaxScale);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAlbumFile == null) {
            return;
        }

        Glide.with(getActivity())
                .load(mAlbumFile.path)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        showImage(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        LogUtils.v(TAG, e.getMessage());
                    }
                });


//        try {
//            ImageSource imageSource = ImageSource.uri(Uri.fromFile(new File(mAlbumFile.path)));
//            Bitmap bitmap = BitmapFactory.decodeFile(mAlbumFile.path);
//            int screenWidth = DeviceUtils.getScreenWidth();
//            if (bitmap.getHeight() > bitmap.getWidth() * 3) {  //长图
//                mImageView.setImage(imageSource, new ImageViewState(1.0F, new PointF(0, 0), 0));
//                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
//            } else if (screenWidth > bitmap.getWidth()) {
//                float scale = (float) screenWidth / bitmap.getWidth();
//                mImageView.setImage(imageSource, new ImageViewState(scale, new PointF(0, 0), 0));
//                mImageView.setMaxScale(scale * mMaxScale);
//                mImageView.setDoubleTapZoomScale(scale * mDoubleClickScale);
//            } else {
//                mImageView.setImage(imageSource);
//                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void showImage(Bitmap bitmap) {
        ImageSource imageSource = ImageSource.bitmap(bitmap);
        int screenWidth = DeviceUtils.getScreenWidth();
        if (bitmap.getHeight() > bitmap.getWidth() * 3) {  //长图
            mImageView.setImage(imageSource, new ImageViewState(1.0F, new PointF(0, 0), 0));
            mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        } else if (screenWidth > bitmap.getWidth()) {
            float scale = (float) screenWidth / bitmap.getWidth();
            mImageView.setImage(imageSource, new ImageViewState(scale, new PointF(0, 0), 0));
            mImageView.setMaxScale(scale * mMaxScale);
            mImageView.setDoubleTapZoomScale(scale * mDoubleClickScale);
        } else {
            mImageView.setImage(imageSource);
            mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        }
    }
}
