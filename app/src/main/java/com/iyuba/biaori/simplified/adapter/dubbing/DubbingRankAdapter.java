package com.iyuba.biaori.simplified.adapter.dubbing;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;

import java.util.List;

public class DubbingRankAdapter extends BaseQuickAdapter<DubbingRankBean.DataDTO, BaseViewHolder> {


    public DubbingRankAdapter(int layoutResId, @Nullable List<DubbingRankBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DubbingRankBean.DataDTO item) {

        ImageView dr_civ = helper.getView(R.id.dr_civ);
        Glide.with(dr_civ.getContext()).load(item.getImgSrc()).into(dr_civ);
        //用户名
        helper.setText(R.id.dr_tv_name, item.getUserName());
        //d得分
        helper.setText(R.id.dr_tv_score, "分数：" + item.getScore());
        //排名
        helper.setText(R.id.dr_tv_num, (helper.getBindingAdapterPosition() + 1) + "");
    }
}
