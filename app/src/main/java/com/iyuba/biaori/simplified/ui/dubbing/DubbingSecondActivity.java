package com.iyuba.biaori.simplified.ui.dubbing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.dubbing.DubbingSecondAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityDubbingSecondBinding;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.presenter.dubbing.DubbingSecondPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.util.TimeUtils;
import com.iyuba.biaori.simplified.view.dubbing.DubbingSecondContract;

import java.util.ArrayList;

/**
 * 配音口语秀-二级列表
 */
public class DubbingSecondActivity extends BaseActivity<DubbingSecondContract.DubbingSecondView, DubbingSecondContract.DubbingSecondPresenter>
        implements DubbingSecondContract.DubbingSecondView {


    private ActivityDubbingSecondBinding activityDubbingSecondBinding;

    private DubbingSecondAdapter dubbingSecondAdapter;

    private int seriesid;
    private String title;

    private static String SERIES_ID = "SERIES_ID";

    private static String TITLE = "TITLE";

    private LineItemDecoration lineItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();

        activityDubbingSecondBinding.toolbar.toolbarIvTitle.setText(title + "");

        activityDubbingSecondBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        dubbingSecondAdapter = new DubbingSecondAdapter(R.layout.item_dubbing_second, new ArrayList<>());
        activityDubbingSecondBinding.dsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityDubbingSecondBinding.dsRv.setAdapter(dubbingSecondAdapter);
        dubbingSecondAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                SeriesBean.DataDTO dataDTO = dubbingSecondAdapter.getItem(position);
                DubbingVideoActivity.startActivity(DubbingSecondActivity.this, dataDTO.getUrl()
                        , dataDTO.getId(), dataDTO.getCategory(), dataDTO.getSound(), dataDTO.getPic(), dataDTO.getTitle());
            }
        });


        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
        activityDubbingSecondBinding.dsRv.addItemDecoration(lineItemDecoration);

        String sign = MD5Util.MD5("iyuba" + TimeUtils.getDays() + "series");
        presenter.getTitleBySeriesid("title", seriesid, sign, "json");
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            seriesid = bundle.getInt(SERIES_ID, 0);
            title = bundle.getString(TITLE);
        }
    }

    /**
     * @param activity
     * @param seriesid 从一级列表中获取的id
     */
    public static void startActivity(Activity activity, int seriesid, String title) {

        Intent intent = new Intent(activity, DubbingSecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SERIES_ID, seriesid);
        bundle.putString(TITLE, title);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public View initLayout() {
        activityDubbingSecondBinding = ActivityDubbingSecondBinding.inflate(getLayoutInflater());
        return activityDubbingSecondBinding.getRoot();
    }

    @Override
    public DubbingSecondContract.DubbingSecondPresenter initPresenter() {
        return new DubbingSecondPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTitleBySeriesidComplete(SeriesBean seriesBean) {


        dubbingSecondAdapter.setNewData(seriesBean.getData());
    }
}