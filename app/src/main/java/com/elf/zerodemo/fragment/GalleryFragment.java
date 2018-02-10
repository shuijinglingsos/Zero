package com.elf.zerodemo.fragment;

import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.elf.zero.utils.DeviceUtils;
import com.elf.zero.utils.LogUtils;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;

import pl.droidsonroids.gif.GifDrawable;

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

    private ViewGroup mRootView;

    private AlbumFile mAlbumFile;

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
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAlbumFile == null) {
            return;
        }

        if (getContext() == null) {
            return;
        }

        if (TextUtils.isEmpty(mAlbumFile.path)) {
            return;
        }

        if (mAlbumFile.fileType == AlbumFile.TYPE_IMAGE) {  //图片
            LogUtils.v(TAG, "--图片地址：" + mAlbumFile.path);
            if (mAlbumFile.path.endsWith(".gif")) {  //gif
                try {
                    GifDrawable gifDrawable = new GifDrawable(mAlbumFile.path);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(gifDrawable);
                    mRootView.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                showImage(mAlbumFile.path);
            }
        } else {  //视频
            LogUtils.v(TAG, "--视频地址：" + mAlbumFile.path);
            ImageView imageView = new ImageView(getContext());
            mRootView.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            Glide.with(getContext())
                    .load(mAlbumFile.path)
                    .asBitmap()
                    .into(imageView);
        }
    }

    private void showImage(String path) {

        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(getContext());
        imageView.setDoubleTapZoomDuration(mZoomDuration);
        imageView.setDoubleTapZoomScale(mDoubleClickScale);
        imageView.setMaxScale(mMaxScale);

        ImageSource imageSource = ImageSource.uri(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//这个参数设置为true才有效，
        BitmapFactory.decodeFile(path, options);//这里的bitmap是个空

        int widget = options.outWidth;
        int height = options.outHeight;

        int screenWidth = DeviceUtils.getScreenWidth();
        if (height > widget * 3) {  //长图
            imageView.setImage(imageSource, new ImageViewState(1.0F, new PointF(0, 0), 0));
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        } else if (screenWidth > widget) {
            float scale = (float) screenWidth / widget;
            imageView.setImage(imageSource, new ImageViewState(scale, new PointF(0, 0), 0));
            imageView.setMaxScale(scale * mMaxScale);
            imageView.setDoubleTapZoomScale(scale * mDoubleClickScale);
        } else {
            imageView.setImage(imageSource);
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        }

        mRootView.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
