package com.elf.zero.image;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import top.zibin.luban.Luban;

/**
 * 图片压缩
 * Created by Lidong on 2018/1/2.
 */
public class ImageCompress {

    private Context mContext;

    private ImageCompress(Context context) {
        mContext = context;
    }

    public File compress(File srcImg) throws IOException {
        return Luban.with(mContext).load(srcImg).get().get(0);
    }

    public static ImageCompress Build(Context context) {
        return new ImageCompress(context);
    }
}
