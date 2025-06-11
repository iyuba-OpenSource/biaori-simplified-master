package com.iyuba.biaori.simplified.adapter.textbook;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;

import java.util.List;

public class RankingAdapter extends BaseQuickAdapter<AudioRankingBean.DataDTO, BaseViewHolder> {


    public RankingAdapter(int layoutResId, @Nullable List<AudioRankingBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AudioRankingBean.DataDTO item) {

        //排名
        helper.setText(R.id.ranking_tv_index, item.getRanking() + "");

        //头像
        ImageView ranking_iv_portrait = helper.getView(R.id.ranking_iv_portrait);
        Glide.with(MyApplication.getContext()).load(item.getImgSrc()).into(ranking_iv_portrait);
        //名字
        helper.setText(R.id.ranking_tv_name, item.getName());
        //句子
        helper.setText(R.id.ranking_tv_sentence, "句子数：" + item.getCount() + "");
        //平均分
        helper.setText(R.id.ranking_tv_avg, "平均分：" + item.getScores() / item.getCount() + "");
        //总分
        helper.setText(R.id.ranking_tv_score, item.getScores() + "分");

    }
}
