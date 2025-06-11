package com.iyuba.biaori.simplified.adapter.dubbing;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.dubbing.MyDubbingBean;

import java.util.List;

public class MyDubbingAdapter extends BaseQuickAdapter<MyDubbingBean.DataDTO, BaseViewHolder> {

    public MyDubbingAdapter(int layoutResId, @Nullable List<MyDubbingBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyDubbingBean.DataDTO item) {

        //图片
        ImageView mydubbing_iv_pic = helper.getView(R.id.mydubbing_iv_pic);
        Glide.with(mydubbing_iv_pic.getContext()).load(item.getPic()).into(mydubbing_iv_pic);

        //title
        helper.setText(R.id.mydubbing_tv_title, item.getTitle());
        helper.setText(R.id.mydubbing_tv_title_ch, item.getTitleCn());
        //删除
        helper.addOnClickListener(R.id.mydubbing_tv_delete);
    }
}
