package com.elf.zero.image;

import java.io.Serializable;

/**
 * 图片选择参数
 */
public class ImageChooseParameter implements Serializable {

    /**
     * 是否使用自定义相册 true使用自定义，false使用系统接口
     */
    public boolean useCustomAlbum;

    /**
     * 0 选择  1相册   2相机
     */
    public int openType;

    /**
     * 回调监听
     */
    public int listener;

    /**
     * 是否裁剪(只有选择一张图片时可用）
     */
    public boolean isCrop;
    /**
     * 裁剪宽度
     */
    public int aspectX;
    /**
     * 裁剪高度
     */
    public int aspectY;

    /**
     * 裁剪输出宽度
     */
    public int outputX;
    /**
     * 裁剪输出高度
     */
    public int outputY;

    /**
     * 选择数量（只有选择使用自定义相册时才管用）(默认为9)
     */
    public int selectCount = 9;
}
