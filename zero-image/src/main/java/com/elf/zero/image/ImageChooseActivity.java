package com.elf.zero.image;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.elf.zero.app.BaseActivity;
import com.elf.zero.utils.FileUtils;
import com.elf.zero.utils.LogUtils;

import java.io.File;
import java.util.Calendar;

/**
 * 选择图片
 * Created by Lidong on 2018/3/12.
 */
public class ImageChooseActivity extends BaseActivity {

    private final static String TAG = ImageChooseActivity.class.getSimpleName();

    private final static int REQUEST_CODE_SYSTEM_ALBUM = 1;
    private final static int REQUEST_CODE_SYSTEM_CAMERA = 2;
    private final static int REQUEST_CODE_SYSTEM_CROP = 3;
    private final static int REQUEST_CODE_CUSTOM_ALBUM = 4;

    private ImageChoose.ImageChooseParameter mParameter;
    private String mImageCachePath;
    private String mSavePath;  //拍照保存路径
    private String mCropPath;  //裁剪路径

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mParameter = (ImageChoose.ImageChooseParameter) intent.getSerializableExtra("data");
        if (mParameter == null) {
            finish();
            return;
        }

        mImageCachePath = FileUtils.getCacheDir() + File.separator + "image";

        if (mParameter.openType == ImageChoose.OPEN_TYPE_SELECT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(new String[]{"从相册中选择", "拍照"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        openAlbum();
                    } else {
                        openCamera();
                    }
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 200);
                }
            });
            builder.show();
            return;
        }

        if (mParameter.openType == ImageChoose.OPEN_TYPE_ALBUM) {
            openAlbum();
            return;
        }

        if (mParameter.openType == ImageChoose.OPEN_TYPE_CAMERA) {
            openCamera();
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if (mParameter.useCustomAlbum) {
            //打开自定义相册
            AlbumActivity.open(this, mParameter.selectCount, REQUEST_CODE_CUSTOM_ALBUM);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SYSTEM_ALBUM);
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        mSavePath = mImageCachePath + File.separator + "camera_" + Calendar.getInstance().getTimeInMillis() + ".jpg";

        File saveFile = new File(mSavePath);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        LogUtils.v(TAG, "打开相机：" + mSavePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathToUri(mSavePath));
        startActivityForResult(intent, REQUEST_CODE_SYSTEM_CAMERA);
    }

    /**
     * 裁剪
     */
    private void openCrop(Uri uri) {
        mCropPath = mImageCachePath + File.separator + "crop_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        File saveFile = new File(mCropPath);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        LogUtils.v(TAG, "裁剪路径：" + mCropPath);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX aspectY 是高宽的比例
        if (mParameter.aspectX > 0 && mParameter.aspectY > 0) {
            intent.putExtra("aspectX", mParameter.aspectX);//宽
            intent.putExtra("aspectY", mParameter.aspectY);//高
        }

        // outputX outputY 是裁剪图片宽高
        if (mParameter.outputX > 0 && mParameter.outputY > 0) {
            intent.putExtra("outputX", mParameter.outputX);
            intent.putExtra("outputY", mParameter.outputY);
        }
        intent.putExtra("return-data", false);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));

        startActivityForResult(intent, REQUEST_CODE_SYSTEM_CROP);
    }

    /**
     * 根据文件实际路径获取URI
     *
     * @param path 文件路径
     * @return URI
     */
    private Uri pathToUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //7.0及以上兼容
            return FileProvider.getUriForFile(this, "com.elf.zero.image.FileProvider", new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    /**
     * 根据URI找到文件实际路径
     *
     * @param uri uri
     * @return 文件实际路径
     */
    private String uriToPath(Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.toString())) {
            return "";
        }

        String filePath = "";

        if (uri.toString().startsWith("file://")) {
            filePath = uri.toString().replaceFirst("file://", "");
            return filePath;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //picturePath就是图片在储存卡所在的位置
            filePath = cursor.getString(columnIndex);
        }
        if (cursor != null) {
            cursor.close();
        }
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String[] paths = null;
            if (requestCode == REQUEST_CODE_SYSTEM_ALBUM) {     //相册选择回调
                Uri uri = data.getData();
                LogUtils.v(TAG, "uri = " + uri.toString());
                if (mParameter.isCrop) {
                    openCrop(uri);
                    return;
                }
                paths = new String[]{uriToPath(uri)};
            } else if (requestCode == REQUEST_CODE_SYSTEM_CAMERA) {    //拍照回调
                if (mParameter.isCrop) {
                    openCrop(pathToUri(mSavePath));
                    return;
                }
                paths = new String[]{mSavePath};
            } else if (requestCode == REQUEST_CODE_SYSTEM_CROP) {      //裁剪回调
                paths = new String[]{mCropPath};
            } else if (requestCode == REQUEST_CODE_CUSTOM_ALBUM) {      //自定义相册
                paths = data.getStringArrayExtra("data");
                if (paths != null && paths.length == 1
                        && mParameter.selectCount == 1 && mParameter.isCrop) {
                    //只有选择一个时才能裁剪
                    openCrop(pathToUri(paths[0]));
                    return;
                }
            }
            ImageChoose.chooseListener().choose(mParameter.listener, paths);
        } else {
            ImageChoose.chooseListener().cancel(mParameter.listener);
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        ImageChoose.chooseListener().remove(mParameter.listener);
        super.onDestroy();
    }

    public static void open(Context context, ImageChoose.ImageChooseParameter parameter) {
        Intent intent = new Intent(context, ImageChooseActivity.class);
        intent.putExtra("data", parameter);
        context.startActivity(intent);
    }
}
