package com.absolutelycold.axgle;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs = {"Collections", "All・全部"};
    private Fragment currentFragment = null;
    private Boolean needBlur;
    private SparseArray<Fragment> fragments = new SparseArray<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public TabsPagerAdapter(@NonNull FragmentManager fm, Boolean needBlur) {
        super(fm);
        this.needBlur = needBlur;
        System.out.println("Tabs Blur: " + needBlur);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        //System.out.println("Get Item Blur: " + needBlur);
        switch (position) {
            case 0:
                CollectionVideosFragment collectionVideosFragment = CollectionVideosFragment.newInstance(needBlur);
                return collectionVideosFragment;
            case 1:
                AllVideosFragment allVideosFragment = AllVideosFragment.newInstance(AllVideosFragment.FRAGMENT_ALL, null, needBlur);
                return allVideosFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (currentFragment != object) {
            currentFragment = (Fragment)object;
        }
    }


    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public SparseArray<Fragment> getFragments() {
        return fragments;
    }
}
