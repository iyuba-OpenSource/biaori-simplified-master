package com.iyuba.biaori.simplified.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentAdapter2 extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private String[] title;

    public MyFragmentAdapter2(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentList, String[] title) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
        this.title = title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
