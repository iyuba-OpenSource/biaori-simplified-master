package com.iyuba.biaori.simplified.ui.fragment.dubbing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.dubbing.DubbingListAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentDubbingListBinding;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.presenter.dubbing.DubbingListPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.dubbing.DubbingSecondActivity;
import com.iyuba.biaori.simplified.ui.dubbing.DubbingVideoActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.util.TimeUtils;
import com.iyuba.biaori.simplified.view.dubbing.DubbingListContract;

import java.util.ArrayList;

/**
 * 配音列表-配音的列表
 */
public class DubbingListFragment extends BaseFragment<DubbingListContract.DubbingListView, DubbingListContract.DubbingListPresenter>
        implements DubbingListContract.DubbingListView {

    private FragmentDubbingListBinding fragmentDubbingListBinding;

    private static final String PARENT_ID = "PARENT_ID";

    /**
     * parent_id
     */
    private int parent_id;

    private DubbingListAdapter dubbingListAdapter;

    private LineItemDecoration lineItemDecoration;

    public DubbingListFragment() {
    }

    public static DubbingListFragment newInstance(int parent_id) {

        DubbingListFragment fragment = new DubbingListFragment();
        Bundle args = new Bundle();
        args.putInt(PARENT_ID, parent_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parent_id = getArguments().getInt(PARENT_ID);
        }
        lineItemDecoration = new LineItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentDubbingListBinding = FragmentDubbingListBinding.inflate(inflater, container, false);
        return fragmentDubbingListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dubbingListAdapter = new DubbingListAdapter(R.layout.item_dubbing_list, new ArrayList<>());

        if (parent_id == 326) {//新闻

            dubbingListAdapter.setIs326(true);
        } else {
            dubbingListAdapter.setIs326(false);
        }

        fragmentDubbingListBinding.dubbinglistRv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentDubbingListBinding.dubbinglistRv.setAdapter(dubbingListAdapter);

        if (fragmentDubbingListBinding.dubbinglistRv.getItemDecorationCount() == 0) {

            lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
            fragmentDubbingListBinding.dubbinglistRv.addItemDecoration(lineItemDecoration);
        }

        dubbingListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                SeriesBean.DataDTO dataDTO = dubbingListAdapter.getData().get(position);
                if (dubbingListAdapter.isIs326()) {


                    DubbingVideoActivity.startActivity(getActivity(), null, dataDTO.getVoaId(), dataDTO.getCategory(),
                            dataDTO.getSound(), dataDTO.getPicX(), dataDTO.getTitleCn());
//                    DubbingSecondActivity.startActivity(getActivity(), Integer.parseInt(dataDTO.getVoaId()), dataDTO.getDescCn());
                } else {

                    DubbingSecondActivity.startActivity(getActivity(), Integer.parseInt(dataDTO.getId()), dataDTO.getDescCn());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String sign = MD5Util.MD5("iyuba" + TimeUtils.getDays() + "series");

        if (parent_id == 326) {

            presenter.titleJpPeiYinApi("android", "json", 1, 1000, 0, parent_id);
        } else {

            presenter.getTitleBySeries("category", parent_id, sign, "json");
        }
    }

    @Override
    protected DubbingListContract.DubbingListPresenter initPresenter() {
        return new DubbingListPresenter();
    }

    @Override
    public void showLoading(String msg) {

        fragmentDubbingListBinding.dubbinglistPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        fragmentDubbingListBinding.dubbinglistPb.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTitleBySeriesComplete(SeriesBean seriesBean) {

        dubbingListAdapter.setNewData(seriesBean.getData());
    }
}