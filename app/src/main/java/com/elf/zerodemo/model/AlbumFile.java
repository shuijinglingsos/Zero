package com.elf.zerodemo.model;

import android.support.annotation.IntDef;

/**
 * 相册文件
 * Created by Lidong on 2018/1/3.
 */
public class AlbumFile {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    @IntDef({TYPE_IMAGE, TYPE_VIDEO})
    public @interface FileType {
    }

    public long id;
    public String path;
    public String name;
    public String title;
    public int bucketId;
    public String bucketName;
    public String mimeType;
    public long addDate;
    public long modifyDate;
    public float latitude;
    public float longitude;
    public long size;

    public long duration;
    public int width;
    public int height;

    @AlbumFile.FileType
    public int fileType;


    public boolean checked;
}
