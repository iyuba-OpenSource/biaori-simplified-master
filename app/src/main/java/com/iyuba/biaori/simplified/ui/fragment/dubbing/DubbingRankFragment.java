package com.iyuba.biaori.simplified.ui.fragment.dubbing;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.dubbing.DubbingRankAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentDubbingRankBinding;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;
import com.iyuba.biaori.simplified.presenter.dubbing.DubbingRankPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.dubbing.MyDubbingDetailsActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.view.dubbing.DubbingRankContract;

import java.util.ArrayList;

/**
 * 配音秀-排行榜
 */
public class DubbingRankFragment extends BaseFragment<DubbingRankContract.DubbingRankView, DubbingRankContract.DubbingRankPresenter>
        implements DubbingRankContract.DubbingRankView {

    private DubbingRankAdapter dubbingRankAdapter;


    private String id;

    private static String ID = "ID";

    private FragmentDubbingRankBinding fragmentDubbingRankBinding;

    private int pageNumber;

    private LineItemDecoration lineItemDecoration;

    public DubbingRankFragment() {
        // Required empty public constructor
    }

    public static DubbingRankFragment newInstance(String id) {
        DubbingRankFragment fragment = new DubbingRankFragment();
        Bundle args = new Bundle();
        args.putString(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ID);
        }

        lineItemDecoration = new LineItemDecoration(MyApplication.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentDubbingRankBinding = FragmentDubbingRankBinding.inflate(inflater, container, false);
        return fragmentDubbingRankBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dubbingRankAdapter = new DubbingRankAdapter(R.layout.item_dubbing_rank, new ArrayList<>());
        fragmentDubbingRankBinding.drRv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentDubbingRankBinding.drRv.setAdapter(dubbingRankAdapter);
        if (fragmentDubbingRankBinding.drRv.getItemDecorationCount() == 0) {

            fragmentDubbingRankBinding.drRv.addItemDecoration(lineItemDecoration);
        }
        dubbingRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                DubbingRankBean.DataDTO dataDTO = dubbingRankAdapter.getItem(position);
                MyDubbingDetailsActivity.startActivity(getActivity(), dataDTO.getVideoUrl(), dataDTO.getTitle(), "");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNumber = 1;
        presenter.getDubbingRank("android", "json", 60001, id,
                pageNumber, 20, 1, "biaori", 3);
    }

    @Override
    protected DubbingRankContract.DubbingRankPresenter initPresenter() {
        return new DubbingRankPresenter();
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
    public void getDubbingRankComplete(DubbingRankBean dubbingRankBean) {

        dubbingRankAdapter.setNewData(dubbingRankBean.getData());
    }
}