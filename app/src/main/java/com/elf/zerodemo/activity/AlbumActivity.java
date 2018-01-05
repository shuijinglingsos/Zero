package com.elf.zerodemo.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.elf.zerodemo.R;
import com.elf.zerodemo.adapter.AlbumFileListAdapter;
import com.elf.zerodemo.adapter.AlbumFolderListAdapter;
import com.elf.zerodemo.model.AlbumFile;
import com.elf.zerodemo.model.AlbumFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片选择
 */
public class AlbumActivity extends AppBaseActivity {

    private View mFlFolders;
    private TextView mTvFolderName;
    private AbsListView mFileGridView, mFolderListView;
    private AlbumFileListAdapter mAlbumFileListAdapter;
    private AlbumFolderListAdapter mAlbumFolderListAdapter;
    private List<AlbumFolder> mAlbumFolders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        init();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permission, 1);
            return;
        }
        showData();
    }

    private void init() {
        mFlFolders = findViewById(R.id.fl_folders);
        mFileGridView = (AbsListView) findViewById(R.id.gv_files);
        mFolderListView = (AbsListView) findViewById(R.id.lv_folders);
        mTvFolderName = (TextView) findViewById(R.id.tv_folder_name);

        mFileGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(mAlbumFileListAdapter.getItem(position).path);
            }
        });
        mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAlbumFolderListAdapter.setSelectedIndex(position);
                mAlbumFolderListAdapter.notifyDataSetChanged();
                showAlbumFiles(mAlbumFolderListAdapter.getItem(position));
                mFlFolders.setVisibility(View.GONE);
            }
        });
    }

    public void onShowFolderList(View view) {
        if (mFlFolders.getVisibility() == View.VISIBLE) {
            mFlFolders.setVisibility(View.GONE);
        } else {
            mFlFolders.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示数据
     */
    private void showData() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            return;
        }

        readAlbumFiles();
        showAlbumFiles(mAlbumFolders.get(0));
        showAlbumFolders();
    }

    /**
     * 显示文件列表
     *
     * @param folder 文件夹
     */
    private void showAlbumFiles(AlbumFolder folder) {
        mTvFolderName.setText(folder.name);
        mAlbumFileListAdapter = new AlbumFileListAdapter(this);
        mAlbumFileListAdapter.addAll(folder.albumFiles);
        mFileGridView.setAdapter(mAlbumFileListAdapter);
    }

    /**
     * 显示文件夹列表
     */
    private void showAlbumFolders() {
        mAlbumFolderListAdapter = new AlbumFolderListAdapter(this);
        mAlbumFolderListAdapter.addAll(mAlbumFolders);
        mFolderListView.setAdapter(mAlbumFolderListAdapter);
    }


    /**
     * 读取相册文件
     */
    private void readAlbumFiles() {

        List<AlbumFile> allAlbumFiles = new ArrayList<>();
        List<AlbumFile> allVideoFiles = new ArrayList<>();
        Map<Integer, AlbumFolder> albumFolderMap = new HashMap<>();

        //读取图片
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(IMAGES[0]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                AlbumFile imageFile = new AlbumFile();
                imageFile.fileType = AlbumFile.TYPE_IMAGE;
                imageFile.path = path;
                imageFile.name = cursor.getString(cursor.getColumnIndex(IMAGES[1]));
                imageFile.title = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
                imageFile.bucketId = cursor.getInt(cursor.getColumnIndex(IMAGES[3]));
                imageFile.bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[4]));
                imageFile.mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
                imageFile.addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[6]));
                imageFile.modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));
                imageFile.latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[8]));
                imageFile.longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[9]));
                imageFile.size = cursor.getLong(cursor.getColumnIndex(IMAGES[10]));
                imageFile.id = cursor.getLong(cursor.getColumnIndex(IMAGES[11]));
                allAlbumFiles.add(imageFile);

                AlbumFolder albumFolder = albumFolderMap.get(imageFile.bucketId);
                if (albumFolder == null) {
                    albumFolder = new AlbumFolder();
                    albumFolder.id = imageFile.bucketId;
                    albumFolder.name = imageFile.bucketName;
                    albumFolder.albumFiles.add(imageFile);
                    albumFolderMap.put(imageFile.bucketId, albumFolder);
                    mAlbumFolders.add(albumFolder);
                } else {
                    albumFolder.albumFiles.add(imageFile);
                }
            }
            cursor.close();
        }

        //读取视频
        cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEOS,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " desc");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(VIDEOS[0]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                AlbumFile videoFile = new AlbumFile();
                videoFile.fileType = AlbumFile.TYPE_VIDEO;
                videoFile.path = path;
                videoFile.name = cursor.getString(cursor.getColumnIndex(VIDEOS[1]));
                videoFile.title = cursor.getString(cursor.getColumnIndex(VIDEOS[2]));
                videoFile.bucketId = cursor.getInt(cursor.getColumnIndex(VIDEOS[3]));
                videoFile.bucketName = cursor.getString(cursor.getColumnIndex(VIDEOS[4]));
                videoFile.mimeType = cursor.getString(cursor.getColumnIndex(VIDEOS[5]));
                videoFile.addDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[6]));
                videoFile.modifyDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[7]));
                videoFile.latitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[8]));
                videoFile.longitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[9]));
                videoFile.size = cursor.getLong(cursor.getColumnIndex(VIDEOS[10]));
                videoFile.duration = cursor.getLong(cursor.getColumnIndex(VIDEOS[11]));
                String resolution = cursor.getString(cursor.getColumnIndex(VIDEOS[12]));

                int width = 0, height = 0;
                if (!TextUtils.isEmpty(resolution) && resolution.contains("x")) {
                    String[] resolutionArray = resolution.split("x");
                    width = Integer.valueOf(resolutionArray[0]);
                    height = Integer.valueOf(resolutionArray[1]);
                }
                videoFile.width = width;
                videoFile.height = height;

                allAlbumFiles.add(videoFile);
                allVideoFiles.add(videoFile);
            }
            cursor.close();
        }

        Collections.sort(allAlbumFiles, new Comparator<AlbumFile>() {
            @Override
            public int compare(AlbumFile o1, AlbumFile o2) {
                return Long.valueOf(o2.addDate).compareTo(o1.addDate);  // o2.addDate > o1.addDate ? 1 : 0;
            }
        });

        AlbumFolder videoFolder = new AlbumFolder();
        videoFolder.id = 99998;
        videoFolder.name = "所有视频";
        videoFolder.albumFiles.addAll(allVideoFiles);
        mAlbumFolders.add(0, videoFolder);

        AlbumFolder albumFolder = new AlbumFolder();
        albumFolder.id = 99999;
        albumFolder.name = "图片和视频";
        albumFolder.albumFiles.addAll(allAlbumFiles);
        mAlbumFolders.add(0, albumFolder);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        showData();
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

    /**
     * Video attribute.
     */
    private static final String[] VIDEOS = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.RESOLUTION
    };

//    视频缩略图在sd卡已经有了 不需要自己创建
//    Cursor cursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
//            , null
//            , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
//            , new String[]{videoId+""}
//            , null);

}
