package com.iyuba.biaori.simplified.ui.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.ActivityHeadlineVideoBinding;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;

/**
 * 视频模块
 */
public class HeadlineVideoActivity extends AppCompatActivity {

    private ActivityHeadlineVideoBinding activityHeadlineVideoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHeadlineVideoBinding = ActivityHeadlineVideoBinding.inflate(getLayoutInflater());
        setContentView(activityHeadlineVideoBinding.getRoot());

        String[] types = {HeadlineType.SMALLVIDEO_JP, HeadlineType.JAPANVIDEOS};
        Bundle bundle = DropdownTitleFragmentNew.buildArguments(10, types, true);
        DropdownTitleFragmentNew fragment = DropdownTitleFragmentNew.newInstance(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.hv_fl, fragment);
        fragmentTransaction.commit();
    }
}