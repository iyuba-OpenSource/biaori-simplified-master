package com.iyuba.biaori.simplified.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.MyFragmentAdapter2;
import com.iyuba.biaori.simplified.databinding.FragmentDubbingBinding;
import com.iyuba.biaori.simplified.ui.fragment.dubbing.DubbingListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 配音页面
 */
public class DubbingFragment extends Fragment {

    private FragmentDubbingBinding fragmentDubbingBinding;

    private MyFragmentAdapter2 myFragmentAdapter2;

    private String titleStr[] = {"新闻", "电影", "动漫", "日剧", "歌曲", "综艺", "纪录片"};

    private int parent_id[] = {326, 325, 324, 323, 327, 328, 329};

    public DubbingFragment() {
    }

    public static DubbingFragment newInstance() {
        DubbingFragment fragment = new DubbingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDubbingBinding = FragmentDubbingBinding.inflate(inflater, container, false);
        return fragmentDubbingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titleStr.length; i++) {

            fragmentList.add(DubbingListFragment.newInstance(parent_id[i]));
        }

        myFragmentAdapter2 = new MyFragmentAdapter2(getChildFragmentManager()
                , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                , fragmentList
                , titleStr);
        fragmentDubbingBinding.dubbingVp.setAdapter(myFragmentAdapter2);

        fragmentDubbingBinding.dubbingTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragmentDubbingBinding.dubbingTl.setupWithViewPager(fragmentDubbingBinding.dubbingVp);
    }

}