package com.iyuba.biaori.simplified.adapter.textbook;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.textbook.RankingDetailsBean;

import java.util.List;

public class RankingDetailsAdapter extends BaseQuickAdapter<RankingDetailsBean.DataDTO, BaseViewHolder> {

    private String name;

    private String portrait;

    public RankingDetailsAdapter(int layoutResId, @Nullable List<RankingDetailsBean.DataDTO> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, RankingDetailsBean.DataDTO item) {

        //头像
        ImageView rd_iv_portrait = helper.getView(R.id.rd_iv_portrait);
        Glide.with(rd_iv_portrait.getContext()).load(portrait).into(rd_iv_portrait);
        //名字
        helper.setText(R.id.rd_tv_name, name);

        //日期
        helper.setText(R.id.rd_tv_date, item.getCreateDate());
        //单句  合成
        if (item.getShuoshuotype() == 2) {

            helper.setText(R.id.ed_tv_kind, "单句");
        } else {
            helper.setText(R.id.ed_tv_kind, "合成");
        }
        //句子
//        helper.setText(R.id.ed_tv_sentence,item.getsen )
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
