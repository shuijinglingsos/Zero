package com.elf.zero.image;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册文件夹
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFolder {

    public int id;
    public String name;

    public List<AlbumFile> albumFiles = new ArrayList<>();
}
