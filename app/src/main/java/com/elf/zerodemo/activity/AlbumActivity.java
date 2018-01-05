package com.elf.zerodemo.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;
import com.elf.zerodemo.model.AlbumFolder;
import com.elf.zerodemo.widget.AlbumFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片选择
 */
public class AlbumActivity extends AppBaseActivity {

    private TextView mTxtDir;
    private AbsListView mGridView;
    private ImageGridAdapter mImageGridAdapter;
    private List<AlbumFolder> mAlbumFolder=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mGridView = (AbsListView) findViewById(R.id.gridView);
        mTxtDir = (TextView) findViewById(R.id.tv_dir);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permission, 1);
            return;
        }

        showData();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(mImageGridAdapter.getItem(position).path);
            }
        });
    }

    private void showData() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor != null) {
            List<AlbumFile> albumFiles = new ArrayList<>();
            Map<String, AlbumFolder> albumFolderMap = new HashMap<>();

            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(IMAGES[0]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                AlbumFile albumFile = new AlbumFile();
                albumFile.path = path;
                albumFile.name = cursor.getString(cursor.getColumnIndex(IMAGES[1]));
                albumFile.title = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
                albumFile.bucketId = cursor.getInt(cursor.getColumnIndex(IMAGES[3]));
                albumFile.bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[4]));
                albumFile.mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
                albumFile.addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[6]));
                albumFile.modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));
                albumFile.latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[8]));
                albumFile.longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[9]));
                albumFile.size = cursor.getLong(cursor.getColumnIndex(IMAGES[10]));
                albumFile.id = cursor.getLong(cursor.getColumnIndex(IMAGES[11]));
                albumFiles.add(albumFile);

                AlbumFolder albumFolder = albumFolderMap.get(albumFile.bucketName);
                if (albumFolder == null) {
                    albumFolder = new AlbumFolder();
                    albumFolder.id = albumFile.bucketId;
                    albumFolder.name = albumFile.bucketName;
                    albumFolder.albumFiles.add(albumFile);
                    albumFolderMap.put(albumFile.bucketName, albumFolder);
                    mAlbumFolder.add(albumFolder);
                } else {
                    albumFolder.albumFiles.add(albumFile);
                }
            }
            cursor.close();
            AlbumFolder albumFolder = new AlbumFolder();
            albumFolder.id = 99999;
            albumFolder.name = "图片和视频";
            albumFolder.albumFiles.addAll(albumFiles);
            mAlbumFolder.add(0, albumFolder);

            showAlbumFolder(mAlbumFolder.get(0));
        }
    }

    private void showAlbumFolder(AlbumFolder folder){
        mTxtDir.setText(folder.name);
        mImageGridAdapter = new ImageGridAdapter(this);
        mImageGridAdapter.addAll(folder.albumFiles);
        mGridView.setAdapter(mImageGridAdapter);
    }

    public void onClickBottom(View view) {
        final String[] array = new String[mAlbumFolder.size()];
        int i = 0;
        for (AlbumFolder loop : mAlbumFolder) {
            array[i] = loop.name + "(" + loop.albumFiles.size() + ")";
            i++;
        }

        new AlertDialog.Builder(this)
                .setTitle("文件夹")
                .setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAlbumFolder(mAlbumFolder.get(which));
                    }
                })
                .show();
    }

    private class ImageGridAdapter extends ArrayAdapter<AlbumFile> {

        public ImageGridAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//            if (convertView == null) {
//                convertView = new TextView(getContext());
//            }
//            AlbumFile detail = getItem(position);
//            StringBuilder sb = new StringBuilder();
//            sb.append("id：" + detail.id).append("\n")
//                    .append("名称：" + detail.name).append("(").append(detail.mimeType).append(")").append("\n")
//                    .append("title：" + detail.title).append("\n")
//                    .append("添加时间：" + detail.addDate).append("\n")
//                    .append("修改时间：" + detail.modifyDate).append("\n")
//                    .append("文件夹:" + detail.bucketName).append("(").append(detail.bucketId).append(")").append("\n")
//                    .append("路径：").append(detail.path).append("\n")
//                    .append("封面：").append(detail.fmPaht);
//
//            ((TextView) convertView).setText(sb.toString());

            if (convertView == null) {
                convertView = new AlbumFileItem(getContext());
            }

            ((AlbumFileItem) convertView).setData(getItem(position));

            return convertView;
        }


    }

    /**
     * Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        showData();
    }

//    /**
//     * Video attribute.
//     */
//    private static final String[] VIDEOS = {
//            MediaStore.Video.Media.DATA,
//            MediaStore.Video.Media.DISPLAY_NAME,
//            MediaStore.Video.Media.TITLE,
//            MediaStore.Video.Media.BUCKET_ID,
//            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Video.Media.MIME_TYPE,
//            MediaStore.Video.Media.DATE_ADDED,
//            MediaStore.Video.Media.DATE_MODIFIED,
//            MediaStore.Video.Media.LATITUDE,
//            MediaStore.Video.Media.LONGITUDE,
//            MediaStore.Video.Media.SIZE,
//            MediaStore.Video.Media.DURATION,
//            MediaStore.Video.Media.RESOLUTION
//    };
//
//
//    /**
//     * Scan for image files.
//     */
//    private void scanImageFile() {
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                IMAGES,
//                null,
//                null,
//                MediaStore.Images.Media.DATE_ADDED);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndex(IMAGES[0]));
//
//                File file = new File(path);
//                if (!file.exists() || !file.canRead()) continue;
//
//                String name = cursor.getString(cursor.getColumnIndex(IMAGES[1]));
//                String title = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
//                int bucketId = cursor.getInt(cursor.getColumnIndex(IMAGES[3]));
//                String bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[4]));
//                String mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
//                long addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[6]));
//                long modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));
//                float latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[8]));
//                float longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[9]));
//                long size = cursor.getLong(cursor.getColumnIndex(IMAGES[10]));
//
//                AlbumFile imageFile = new AlbumFile();
//                imageFile.setMediaType(AlbumFile.TYPE_IMAGE);
//                imageFile.setPath(path);
//                imageFile.setName(name);
//                imageFile.setTitle(title);
//                imageFile.setBucketId(bucketId);
//                imageFile.setBucketName(bucketName);
//                imageFile.setMimeType(mimeType);
//                imageFile.setAddDate(addDate);
//                imageFile.setModifyDate(modifyDate);
//                imageFile.setLatitude(latitude);
//                imageFile.setLongitude(longitude);
//                imageFile.setSize(size);
//
//                // Filter.
//                if (mSizeFilter != null && mSizeFilter.filter(size)) {
//                    if (!mFilterVisibility) continue;
//                    imageFile.setEnable(false);
//                }
//                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
//                    if (!mFilterVisibility) continue;
//                    imageFile.setEnable(false);
//                }
//
//                allFileFolder.addAlbumFile(imageFile);
//                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
//
//                if (albumFolder != null)
//                    albumFolder.addAlbumFile(imageFile);
//                else {
//                    albumFolder = new AlbumFolder();
//                    albumFolder.setId(bucketId);
//                    albumFolder.setName(bucketName);
//                    albumFolder.addAlbumFile(imageFile);
//
//                    albumFolderMap.put(bucketName, albumFolder);
//                }
//            }
//            cursor.close();
//        }
//    }
//
//    /**
//     * Scan for image files.
//     */
//    private void scanVideoFile() {
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                VIDEOS,
//                null,
//                null,
//                MediaStore.Video.Media.DATE_ADDED);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndex(VIDEOS[0]));
//
//                File file = new File(path);
//                if (!file.exists() || !file.canRead()) continue;
//
//                String name = cursor.getString(cursor.getColumnIndex(VIDEOS[1]));
//                String title = cursor.getString(cursor.getColumnIndex(VIDEOS[2]));
//                int bucketId = cursor.getInt(cursor.getColumnIndex(VIDEOS[3]));
//                String bucketName = cursor.getString(cursor.getColumnIndex(VIDEOS[4]));
//                String mimeType = cursor.getString(cursor.getColumnIndex(VIDEOS[5]));
//                long addDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[6]));
//                long modifyDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[7]));
//                float latitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[8]));
//                float longitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[9]));
//                long size = cursor.getLong(cursor.getColumnIndex(VIDEOS[10]));
//                long duration = cursor.getLong(cursor.getColumnIndex(VIDEOS[11]));
//                String resolution = cursor.getString(cursor.getColumnIndex(VIDEOS[12]));
//
//                AlbumFile videoFile = new AlbumFile();
//                videoFile.setMediaType(AlbumFile.TYPE_VIDEO);
//                videoFile.setPath(path);
//                videoFile.setName(name);
//                videoFile.setTitle(title);
//                videoFile.setBucketId(bucketId);
//                videoFile.setBucketName(bucketName);
//                videoFile.setMimeType(mimeType);
//                videoFile.setAddDate(addDate);
//                videoFile.setModifyDate(modifyDate);
//                videoFile.setLatitude(latitude);
//                videoFile.setLongitude(longitude);
//                videoFile.setSize(size);
//                videoFile.setDuration(duration);
//
//                int width = 0, height = 0;
//                if (!TextUtils.isEmpty(resolution) && resolution.contains("x")) {
//                    String[] resolutionArray = resolution.split("x");
//                    width = Integer.valueOf(resolutionArray[0]);
//                    height = Integer.valueOf(resolutionArray[1]);
//                }
//                videoFile.setWidth(width);
//                videoFile.setHeight(height);
//
//                // Filter.
//                if (mSizeFilter != null && mSizeFilter.filter(size)) {
//                    if (!mFilterVisibility) continue;
//                    videoFile.setEnable(false);
//                }
//                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
//                    if (!mFilterVisibility) continue;
//                    videoFile.setEnable(false);
//                }
//                if (mDurationFilter != null && mDurationFilter.filter(duration)) {
//                    if (!mFilterVisibility) continue;
//                    videoFile.setEnable(false);
//                }
//
//                allFileFolder.addAlbumFile(videoFile);
//                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
//
//                if (albumFolder != null)
//                    albumFolder.addAlbumFile(videoFile);
//                else {
//                    albumFolder = new AlbumFolder();
//                    albumFolder.setId(bucketId);
//                    albumFolder.setName(bucketName);
//                    albumFolder.addAlbumFile(videoFile);
//
//                    albumFolderMap.put(bucketName, albumFolder);
//                }
//            }
//            cursor.close();
//        }
//    }
//
//    视频缩略图在sd卡已经有了 不需要自己创建
//    Cursor cursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
//            , null
//            , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
//            , new String[]{videoId+""}
//            , null);

}