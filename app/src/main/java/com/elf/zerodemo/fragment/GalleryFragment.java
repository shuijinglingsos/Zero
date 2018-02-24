package com.elf.zerodemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.elf.zero.utils.DeviceUtils;
import com.elf.zero.utils.LogUtils;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;

import pl.droidsonroids.gif.GifDrawable;

/**
 * 画廊fragment
 * Created by Lidong on 2018/2/9.
 */
public class GalleryFragment extends Fragment {

    private final static String TAG = GalleryFragment.class.getSimpleName();

    private final static String ARG_ALBUM_FILE = "arg_album_file";

    private final static float mMaxScale = 3;
    private final static float mDoubleClickScale = 1.5f;
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

            } else {  //一般图片
                showImage(mAlbumFile.path);
            }
        } else {  //视频
            LogUtils.v(TAG, "--视频地址：" + mAlbumFile.path);
            //图片
            ImageView imageView = new ImageView(getContext());
            mRootView.addView(imageView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            Glide.with(getContext())
                    .load(mAlbumFile.path)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                            //播放按钮
                            final ImageView video = new ImageView(getContext());
                            video.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
                            video.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    playVideo(mAlbumFile.path);
                                }
                            });
                            mRootView.addView(video,
                                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

    private void showImage(String path) {

        final SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(getContext());
        imageView.setDoubleTapZoomDuration(mZoomDuration);
        imageView.setDoubleTapZoomScale(mDoubleClickScale);
        imageView.setMaxScale(mMaxScale);

        final ImageSource imageSource = ImageSource.uri(path);
        //获取旋转角度
        boolean rotation = false;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            rotation = orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270;
            LogUtils.v(TAG, "Image orientation :" + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取图片宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//这个参数设置为true才有效，
        BitmapFactory.decodeFile(path, options);//这里的bitmap是个空
        //需要旋转则交换宽高
        int widget = rotation ? options.outHeight : options.outWidth;
        int height = rotation ? options.outWidth : options.outHeight;
        int screenWidth = DeviceUtils.getScreenWidth();
//        int screenHeight = DeviceUtils.getScreenHeight();

        if (height > widget * 3) {  //长图
            imageView.setImage(imageSource, new ImageViewState(1.0F, new PointF(0, 0), -1));
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        } else if (screenWidth > widget) {  //图片宽度小于屏幕宽度，放大到屏幕宽度大小
            float scale = (float) screenWidth / widget;
            imageView.setImage(imageSource, new ImageViewState(scale, new PointF(0, 0), -1));
            imageView.setMaxScale(scale * mMaxScale);
            imageView.setDoubleTapZoomScale(scale * mDoubleClickScale);
        } else {
            imageView.setImage(imageSource);
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        }
        imageView.setOrientation(-1);
        mRootView.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void playVideo(String path) {
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }
}
