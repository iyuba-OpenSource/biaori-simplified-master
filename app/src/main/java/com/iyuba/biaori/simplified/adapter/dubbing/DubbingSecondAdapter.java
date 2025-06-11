package com.iyuba.biaori.simplified.adapter.dubbing;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;

import java.util.List;

import retrofit2.http.HEAD;

public class DubbingSecondAdapter extends BaseQuickAdapter<SeriesBean.DataDTO, BaseViewHolder> {


    public DubbingSecondAdapter(int layoutResId, @Nullable List<SeriesBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SeriesBean.DataDTO item) {

        //图片
        ImageView ds_iv_pic = helper.getView(R.id.ds_iv_pic);
        Glide.with(ds_iv_pic.getContext()).load(item.getPicX()).into(ds_iv_pic);

        //title
        helper.setText(R.id.ds_tv_title_ch, item.getTitleCn());
        helper.setText(R.id.ds_tv_title, item.getTitle());
        //浏览量
        helper.setText(R.id.ds_tv_browse, item.getReadCount());
    }
}
