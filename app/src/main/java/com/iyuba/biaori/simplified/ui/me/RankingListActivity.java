package com.iyuba.biaori.simplified.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.me.RankingListAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityRankingListBinding;
import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;
import com.iyuba.biaori.simplified.presenter.me.RankingListPresenter;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.view.me.RankingListContract;
import com.iyuba.module.toolbox.MD5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的  - 排行榜
 */
public class RankingListActivity extends AppCompatActivity
        implements RankingListContract.RankingListView {

    private ActivityRankingListBinding activityRankingListBinding;

    private RankingListAdapter rankingListAdapter;

    private RankingListPresenter rankingListPresenter;


    private String uid;

    /**
     * 接口公用参数 当天、一周、月
     */
    private String type = "D";

    private LineItemDecoration lineItemDecoration;

    /**
     * 页数
     */
    private int page = 0;

    /**
     * 列表头部
     */
    private View headerView;

    private ImageView ranking_iv_portrait;
    private TextView ranking_tv_name;
    private TextView ranking_tv_myname;
    private TextView ranking_tv_ml1;
    private TextView ranking_tv_ml2;
    private TextView ranking_tv_mr1;
    private TextView ranking_tv_mr2;
    private ImageView ranking_iv_mportrait;

    /**
     * 格式化正确率
     */
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRankingListBinding = ActivityRankingListBinding.inflate(getLayoutInflater());
        setContentView(activityRankingListBinding.getRoot());

        decimalFormat = new DecimalFormat("#00.0");

        initOperation();
        rankingListPresenter = new RankingListPresenter();
        rankingListPresenter.attchView(this);

        activityRankingListBinding.rankingRv.setLayoutManager(new LinearLayoutManager(this));
        rankingListAdapter = new RankingListAdapter(R.layout.item_ranking_list, new ArrayList<>());
        activityRankingListBinding.rankingRv.setAdapter(rankingListAdapter);
        rankingListAdapter.setEnableLoadMore(true);

        //loadmore
        rankingListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                String kind = rankingListAdapter.getKind();
                if (kind.equals(RankingListAdapter.KINDS[0])) {//口语

                    String topic = Constant.bookDataDTO.getName();
                    String sign = MD5.getMD5ofStr(uid + topic + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTopicRanking(uid, type, 10, sign, 10 * page, topic, 0, 4);
                } else if (kind.equals(RankingListAdapter.KINDS[1])) {//听力

                    String topic = Constant.bookDataDTO.getName();
                    String sign = MD5.getMD5ofStr(uid + topic + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "listening");
                } else if (kind.equals(RankingListAdapter.KINDS[2])) {//学习

                    String topic = Constant.bookDataDTO.getName();
                    String sign = MD5.getMD5ofStr(uid + topic + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "all");
                } else {//测试

                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTestRanking(uid, type, 10, sign, 10 * page);
                }
            }
        }, activityRankingListBinding.rankingRv);

        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.space_10dp));
        activityRankingListBinding.rankingRv.addItemDecoration(lineItemDecoration);

        //添加header
        headerView = LayoutInflater.from(getApplication()).inflate(R.layout.header_ranking, null);
        rankingListAdapter.addHeaderView(headerView);
        ranking_iv_portrait = headerView.findViewById(R.id.ranking_iv_portrait);
        ranking_tv_name = headerView.findViewById(R.id.ranking_tv_name);
        ranking_tv_myname = headerView.findViewById(R.id.ranking_tv_myname);
        ranking_tv_ml1 = headerView.findViewById(R.id.ranking_tv_ml1);
        ranking_tv_ml2 = headerView.findViewById(R.id.ranking_tv_ml2);
        ranking_tv_mr1 = headerView.findViewById(R.id.ranking_tv_mr1);
        ranking_tv_mr2 = headerView.findViewById(R.id.ranking_tv_mr2);
        ranking_iv_mportrait = headerView.findViewById(R.id.ranking_iv_mportrait);

        if (Constant.userinfo == null) {

            headerView.setVisibility(View.GONE);
        }

        uid = Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "";
    }

    private void initOperation() {

        List<String> rankingTypeList = new ArrayList<>();
        rankingTypeList.add("本日");
        rankingTypeList.add("本周");
        rankingTypeList.add("本月");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rankingTypeList);
        activityRankingListBinding.rankingSpinnerType.setAdapter(arrayAdapter);
        //参数type
        activityRankingListBinding.rankingSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {//日

                    type = "D";

                } else if (position == 1) {

                    type = "W";
                } else {

                    type = "M";
                }
                page = 0;


                String kind = rankingListAdapter.getKind();
                if (kind.equals(RankingListAdapter.KINDS[0])) {//口语

                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTopicRanking(uid, type, 10, sign, 10 * page, "bbc", 0, 4);
                } else if (kind.equals(RankingListAdapter.KINDS[1])) {//听力

                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "listening");
                } else if (kind.equals(RankingListAdapter.KINDS[2])) {//学习

                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "all");
                } else {//测试

                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTestRanking(uid, type, 10, sign, 10 * page);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //导航栏
        activityRankingListBinding.rankingRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                rankingListAdapter.setNewData(new ArrayList<>());
                page = 0;
                rankingListPresenter.clearDisposable();

                if (checkedId == R.id.ranking_rb_speak) {//口语

                    rankingListAdapter.setKind(RankingListAdapter.KINDS[0]);
                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTopicRanking(uid, type, 10, sign, 10 * page, "bbc", 0, 4);
                } else if (checkedId == R.id.ranking_rb_listening) {//听力

                    rankingListAdapter.setKind(RankingListAdapter.KINDS[1]);
                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "listening");
                } else if (checkedId == R.id.ranking_rb_study) {//学习

                    rankingListAdapter.setKind(RankingListAdapter.KINDS[2]);
                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getStudyRanking(uid, type, 10, sign, 10 * page, "all");
                } else {//测试

                    rankingListAdapter.setKind(RankingListAdapter.KINDS[3]);
                    String sign = MD5.getMD5ofStr(uid + "bbc" + 0 + "0" + "10" + DateUtil.getCurDate());
                    rankingListPresenter.getTestRanking(uid, type, 10, sign, 10 * page);
                }
            }
        });
        activityRankingListBinding.rankingLlType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //back
        activityRankingListBinding.calendarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initTypePopup() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rankingListPresenter != null) {

            rankingListPresenter.detachView();
        }
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

    /**
     * 更新头部信息
     *
     * @param studyRankingBean
     * @param mode
     */
    private void updateHead(StudyRankingBean studyRankingBean, String mode) {

        String l1 = null;
        String l2 = null;
        String r1 = null;
        String r2 = null;

        if (mode.equals(RankingListAdapter.KINDS[0])) {

            l1 = "排名：" + studyRankingBean.getMyranking();
            l2 = "合成配音数：" + studyRankingBean.getMycount();
            r1 = "总分：" + studyRankingBean.getMyscores();
            if (studyRankingBean.getMycount() == 0) {

                r2 = "平均分：" + 0;
            } else {

                r2 = "平均分：" + studyRankingBean.getMyscores() / studyRankingBean.getMycount();
            }
        } else if (mode.equals(RankingListAdapter.KINDS[2])) {

            l1 = "排名：" + studyRankingBean.getMyranking();
            int m = studyRankingBean.getTotalTime() / 60;
            int h = m / 60;
            int s = studyRankingBean.getTotalTime() % 60;
            l2 = h + "小时" + m + "分" + s + "秒";
            r1 = "文章数:" + studyRankingBean.getTotalEssay();
            r2 = "单词数：" + studyRankingBean.getTotalWord();
        } else if (mode.equals(RankingListAdapter.KINDS[1])) {

            l1 = "排名：" + studyRankingBean.getMyranking();
            int m = studyRankingBean.getTotalTime() / 60;
            int h = m / 60;
            m = m % 60;
            int s = studyRankingBean.getTotalTime() % 60;
            if (h == 0 && m == 0) {
                l2 = s + "秒";
            } else if (h == 0) {
                l2 = m + "分钟" + s + "秒";
            } else {
                l2 = h + "小时" + m + "分钟" + s + "秒";
            }
            r1 = "文章数：" + studyRankingBean.getTotalEssay();
            r2 = "单词数：" + studyRankingBean.getTotalWord();
        } else {

            l1 = "排名：" + studyRankingBean.getMyranking();
            l2 = "总体数：" + studyRankingBean.getTotalTest();
            r1 = "正确数:" + studyRankingBean.getTotalRight();
            if (studyRankingBean.getTotalTest() == 0) {

                r2 = "正确率:-";
            } else {
                double pf = 100.0 * studyRankingBean.getTotalRight() / studyRankingBean.getTotalTest();
                String p = decimalFormat.format(pf);
                r2 = "正确率:" + p + "%";
            }
        }
        //自己的头像
        Glide.with(this).load(studyRankingBean.getMyimgSrc()).into(ranking_iv_mportrait);

        StudyRankingBean.DataDTO dataDTO = studyRankingBean.getData().get(0);
        Glide.with(this).load(dataDTO.getImgSrc()).into(ranking_iv_portrait);
        ranking_tv_name.setText(dataDTO.getName());
        ranking_tv_myname.setText(studyRankingBean.getMyname());
        ranking_tv_ml1.setText(l1);
        ranking_tv_ml2.setText(l2);
        ranking_tv_mr1.setText(r1);
        ranking_tv_mr2.setText(r2);
    }

    @Override
    public void getStudyRanking(StudyRankingBean studyRankingBean, String mode) {

        if (mode.equals("listening")) {

            //冠军
            updateHead(studyRankingBean, RankingListAdapter.KINDS[1]);
            rankingListAdapter.setNewData(studyRankingBean.getData());
        } else {
            updateHead(studyRankingBean, RankingListAdapter.KINDS[2]);
            rankingListAdapter.setNewData(studyRankingBean.getData());
        }
    }

    @Override
    public void getTopicRanking(StudyRankingBean studyRankingBean) {

        updateHead(studyRankingBean, RankingListAdapter.KINDS[0]);
        rankingListAdapter.setNewData(studyRankingBean.getData());
    }

    @Override
    public void getTestRanking(StudyRankingBean studyRankingBean) {

        updateHead(studyRankingBean, RankingListAdapter.KINDS[3]);
        rankingListAdapter.setNewData(studyRankingBean.getData());
    }

    @Override
    public void loadmore(StudyRankingBean studyRankingBean, int flag) {

        if (studyRankingBean != null) {

            rankingListAdapter.addData(studyRankingBean.getData());
        }
        if (flag == 0) {

            rankingListAdapter.loadMoreEnd();
        } else if (flag == 1) {

            rankingListAdapter.loadMoreComplete();
        } else {

            rankingListAdapter.loadMoreFail();
        }
    }
}