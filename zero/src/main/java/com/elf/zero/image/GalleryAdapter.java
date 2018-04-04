package com.elf.zero.image;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by Lidong on 2018/2/9.
 */
public class GalleryAdapter extends FragmentStatePagerAdapter {

    private List<AlbumFile> mAlbumFiles;

    public GalleryAdapter(FragmentManager fm, List<AlbumFile> files) {
        super(fm);
        mAlbumFiles = files;
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.get(mAlbumFiles.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }
}
