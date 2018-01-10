package com.elf.zerodemo.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;

import com.elf.zero.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相册数据源
 * Created by Lidong on 2018/1/10.
 */
public class AlbumDataSource {

    private final static String TAG = AlbumDataSource.class.getSimpleName();

    private Context mContext;

    private List<AlbumFolder> mAllFolders = new ArrayList<>();  //文件夹
    private List<AlbumFile> mAllFiles = new ArrayList<>();  //所有图片和视屏文件
    private List<AlbumFile> mAllVideos = new ArrayList<>();
    private SparseArray<AlbumFolder> mAlbumFolderMap = new SparseArray<>();  //文件夹字典

    public AlbumDataSource(Context context) {
        mContext = context;
    }

    public List<AlbumFolder> getFolders(){
        return mAllFolders;
    }

    public void readImageAndVideo() {
        readImages();
        readVideos();
        sort();
        AlbumFolder videoFolder = new AlbumFolder();
        videoFolder.id = Integer.MAX_VALUE;
        videoFolder.name = "所有视频";
        videoFolder.albumFiles.addAll(mAllVideos);
        mAllFolders.add(0, videoFolder);

        AlbumFolder albumFolder = new AlbumFolder();
        albumFolder.id = Integer.MAX_VALUE - 1;
        albumFolder.name = "图片和视频";
        albumFolder.albumFiles.addAll(mAllFiles);
        mAllFolders.add(0, albumFolder);
    }

    private void readImages() {
        //读取图片
        ContentResolver contentResolver = mContext.getContentResolver();
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

                LogUtils.v(TAG, "--image id:" + imageFile.id);
                mAllFiles.add(imageFile);

                AlbumFolder albumFolder = mAlbumFolderMap.get(imageFile.bucketId);
                if (albumFolder == null) {
                    albumFolder = new AlbumFolder();
                    albumFolder.id = imageFile.bucketId;
                    albumFolder.name = imageFile.bucketName;
                    albumFolder.albumFiles.add(imageFile);
                    mAlbumFolderMap.put(imageFile.bucketId, albumFolder);
                    mAllFolders.add(albumFolder);
                } else {
                    albumFolder.albumFiles.add(imageFile);
                }
            }
            cursor.close();
        }
    }

    private void readVideos() {
        //读取视频
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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
                videoFile.id = cursor.getLong(cursor.getColumnIndex(VIDEOS[13]));

                LogUtils.v(TAG, "--video id:" + videoFile.id);

                mAllFiles.add(videoFile);
                mAllVideos.add(videoFile);
            }
            cursor.close();
        }
    }

    private void sort(){
        Collections.sort(mAllFiles, new Comparator<AlbumFile>() {
            @Override
            public int compare(AlbumFile o1, AlbumFile o2) {
                return Long.valueOf(o2.addDate).compareTo(o1.addDate);
            }
        });
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
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media._ID
    };
}
