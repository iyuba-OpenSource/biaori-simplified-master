package com.iyuba.biaori.simplified.ui.fragment.textbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.textbook.RankingAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentRankingBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;
import com.iyuba.biaori.simplified.presenter.textbook.RankingPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.textbook.RankingDetailsActivity;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.view.textbook.RankingContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 课文住界面
 */
public class RankingFragment extends BaseFragment<RankingContract.RankingView, RankingContract.RankingPresenter>
        implements RankingContract.RankingView {

    private FragmentRankingBinding binding;

    /**
     * 课本source
     */
    private int source;

    /**
     * 课程id
     */
    private int lessonid;

    private int page = 1;

    private int pageNum = 10;

    private RankingAdapter rankingAdapter;

    private String bookName = null;

    public RankingFragment() {

    }


    public static RankingFragment newInstance(int source, int lessonid) {

        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putInt("SOURCE", source);
        args.putInt("LESSONID", lessonid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getInt("SOURCE");
            lessonid = getArguments().getInt("LESSONID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //获取课本的name
        List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);

        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }

        int uid = 0;
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid();
        }
        page = 1;
        presenter.getTopicRanking(bookName, lessonid, uid, "D", (page - 1) * pageNum, pageNum,
                MD5Util.MD5(uid + bookName + lessonid + (page - 1) + pageNum + DateUtil.getCurDate()));

        rankingAdapter = new RankingAdapter(R.layout.item_ranking, new ArrayList<>());
        binding.rankingRvList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rankingRvList.setAdapter(rankingAdapter);
        rankingAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                int uid = 0;
                if (Constant.userinfo != null) {

                    uid = Constant.userinfo.getUid();
                }
                presenter.getTopicRanking(bookName, lessonid, uid, "D", (page - 1) * pageNum, pageNum,
                        MD5Util.MD5(uid + bookName + lessonid + (page - 1) + pageNum + DateUtil.getCurDate()));
            }
        }, binding.rankingRvList);

        rankingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                AudioRankingBean.DataDTO dataDTO = rankingAdapter.getItem(position);
                RankingDetailsActivity.startActivity(requireActivity(), source, lessonid, dataDTO.getUid(), dataDTO.getImgSrc(), dataDTO.getName());
            }
        });

        binding.rankingWrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                int uid = 0;
                if (Constant.userinfo != null) {

                    uid = Constant.userinfo.getUid();
                }
                page = 1;
                presenter.getTopicRanking(bookName, lessonid, uid, "D", (page - 1) * pageNum, pageNum,
                        MD5Util.MD5(uid + bookName + lessonid + (page - 1) + pageNum + DateUtil.getCurDate()));
            }
        });
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected RankingContract.RankingPresenter initPresenter() {
        return new RankingPresenter();
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
    public void getTopicRanking(AudioRankingBean audioRankingBean, int start) {

        if (binding.rankingWrl.isRefreshing()) {

            binding.rankingWrl.setRefreshing(false);
        }


        if (start == 0) {

            rankingAdapter.setNewData(audioRankingBean.getData());
        } else {

            rankingAdapter.addData(audioRankingBean.getData());
            int size = audioRankingBean.getResult();
            if (size == pageNum) {

                rankingAdapter.loadMoreComplete();
            } else {

                rankingAdapter.loadMoreEnd();
            }
        }


        if (Constant.userinfo == null) {

            binding.rankingLlMyself.setVisibility(View.GONE);
        } else {

            binding.rankingLlMyself.setVisibility(View.VISIBLE);
            binding.rankingTvIndex.setText(audioRankingBean.getMyranking() + "");

            Glide.with(MyApplication.getContext()).load(audioRankingBean.getMyimgSrc()).into(binding.rankingIvPortrait);
            binding.rankingTvName.setText(audioRankingBean.getMyname());
            binding.rankingTvSentence.setText("句子数：" + audioRankingBean.getMycount());
            int avg = audioRankingBean.getMycount() == 0 ? 0 : audioRankingBean.getMyscores() / audioRankingBean.getMycount();
            binding.rankingTvAvg.setText("平均分" + avg);
            binding.rankingTvScore.setText(audioRankingBean.getMyscores() + "分");
        }


    }
}