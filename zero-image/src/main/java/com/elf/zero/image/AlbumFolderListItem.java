package com.elf.zero.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elf.zero.app.BaseLinearLayout;

/**
 * 文件夹列表项目
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFolderListItem extends BaseLinearLayout {

    private ImageView mIvCover, mIvSelected;
    private TextView mTvName;

    private AlbumFolder mAlbumFolder;

    public AlbumFolderListItem(Context context) {
        super(context);
    }

    public AlbumFolderListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.zero_image_widget_folder_list_item;
    }

    @Override
    protected void initView(AttributeSet attrs) {
        mIvCover = getViewById(R.id.iv_cover);
        mIvSelected = getViewById(R.id.iv_selected);
        mTvName = getViewById(R.id.tv_name);
    }

    public void setData(AlbumFolder folder) {

        if (mAlbumFolder == folder) {
            return;
        }

        mAlbumFolder = folder;

        mTvName.setText(folder.name + "（" + folder.albumFiles.size() + "）");

        AlbumFile albumFile = null;
        if (folder.albumFiles.size() > 0) {
            albumFile = folder.albumFiles.get(0);
        }

        if (albumFile == null) {
            Glide.with(getContext()).load("").centerCrop().dontAnimate().skipMemoryCache(true)
                    .into(mIvCover);
        } else {
            if (albumFile.name.endsWith("gif")) {
                Glide.with(getContext()).load(albumFile.path).asBitmap().centerCrop().dontAnimate().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mIvCover);
            } else {
                Glide.with(getContext()).load(albumFile.path).centerCrop().dontAnimate().skipMemoryCache(true)
                        .into(mIvCover);
            }
        }
    }

    public void setSelected(boolean value) {
        mIvSelected.setVisibility(value ? VISIBLE : GONE);
    }
}
