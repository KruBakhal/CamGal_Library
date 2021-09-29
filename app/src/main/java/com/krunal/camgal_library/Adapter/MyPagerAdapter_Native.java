package com.krunal.camgal_library.Adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.krunal.camgal_library.Frag.Gallery_Fragment_Native;
import com.krunal.camgal_library.GalleryActivity;

import java.util.ArrayList;
import java.util.List;


public class MyPagerAdapter_Native extends FragmentPagerAdapter {

    private final FragmentManager fm;
    private List<Fragment> childFragments;

    public MyPagerAdapter_Native(@NonNull FragmentManager fm, GalleryActivity context, List<Fragment> listFeature) {
        super(fm);
        childFragments = listFeature;
        this.fm = fm;

    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {

        super.destroyItem(viewGroup, i, obj);
    }

    public Fragment getRegisteredFragment(int i) {
        if (childFragments == null || childFragments.size() == 0)
            return null;
        else
            return childFragments.get(i);
    }

    public int addScreen(Gallery_Fragment_Native gallery_fragment) {
        if (childFragments == null)
            childFragments = new ArrayList<>();
        childFragments.add(gallery_fragment);
        notifyDataSetChanged();
        return childFragments.size();
    }

    public int removeScreen() {
        if (childFragments != null && childFragments.size() != 0)
            childFragments.remove(childFragments.size() - 1);
        if (childFragments != null && childFragments.size() != 0)
            notifyDataSetChanged();
        return childFragments.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (childFragments == null || childFragments.size() == 0)
            return null;
        else
            return childFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // We map the requested position to the position as per our screens array list
        Fragment fragment = childFragments.get(position);
        int internalPosition = childFragments.indexOf(fragment);
        return super.instantiateItem(container, internalPosition);
    }

    @Override
    public int getCount() {
        if (childFragments == null || childFragments.size() == 0)
            return 0;
        else return childFragments.size();
    }

    public void clear() {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : childFragments) {
            transaction.remove(fragment);
        }
        childFragments.clear();
        transaction.commitAllowingStateLoss();
    }
}
