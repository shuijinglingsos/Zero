package com.elf.zerodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elf.zero.widget.AbsLinearLayout;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;
import com.elf.zerodemo.model.AlbumFolder;

/**
 * 文件夹列表项目
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFolderItem extends AbsLinearLayout {

    private ImageView mIvCover, mIvSelected;
    private TextView mTvName;

    public AlbumFolderItem(Context context) {
        super(context);
    }

    public AlbumFolderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.widget_album_folder_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mIvCover = getViewById(R.id.iv_cover);
        mIvSelected = getViewById(R.id.iv_selected);
        mTvName = getViewById(R.id.tv_name);
    }

    public void setData(AlbumFolder folder) {
        mTvName.setText(folder.name + "（" + folder.albumFiles.size() + "）");

        AlbumFile albumFile = null;
        if (folder.albumFiles.size() > 0) {
            albumFile = folder.albumFiles.get(0);
        }

        if (albumFile == null) {
            Glide.with(getContext()).load("").centerCrop().skipMemoryCache(true)
                    .into(mIvCover);
        } else {
            if(albumFile.name.endsWith("gif")) {
                Glide.with(getContext()).load(albumFile.path).asBitmap().centerCrop().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mIvCover);
            }else{
                Glide.with(getContext()).load(albumFile.path).centerCrop().skipMemoryCache(true)
                        .into(mIvCover);
            }
        }
    }

    public void setSelected(boolean value) {
        mIvSelected.setVisibility(value ? VISIBLE : GONE);
    }
}
