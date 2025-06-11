package com.iyuba.biaori.simplified.adapter.dubbing;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DubbingListAdapter extends BaseQuickAdapter<SeriesBean.DataDTO, BaseViewHolder> {

    private SimpleDateFormat simpleDateFormat;

    private boolean is326 = false;

    public DubbingListAdapter(int layoutResId, @Nullable List<SeriesBean.DataDTO> data) {
        super(layoutResId, data);

        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd");
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SeriesBean.DataDTO item) {

        ImageView dl_iv_pic = helper.getView(R.id.dl_iv_pic);
        if (is326) {

            Glide.with(dl_iv_pic.getContext()).load(item.getPicX()).into(dl_iv_pic);
            //title
            helper.setText(R.id.dl_tv_title, item.getTitle());
            helper.setText(R.id.dl_tv_title_ch, item.getTitleCn());
            helper.setText(R.id.dl_tv_type, item.getKeyWords());
            String createTime = null;
            try {
                Date date = simpleDateFormat.parse(item.getCreatTime());
                createTime = simpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            helper.setText(R.id.dl_tv_date, createTime);
        } else {

            //图片
            Glide.with(dl_iv_pic.getContext()).load(item.getPic()).into(dl_iv_pic);
            //title
            helper.setText(R.id.dl_tv_title, item.getDescCn());
            helper.setText(R.id.dl_tv_title_ch, item.getSeriesName());
            helper.setText(R.id.dl_tv_type, item.getKeyWords());

            String updateTime = null;
            try {
                Date date = simpleDateFormat.parse(item.getUpdateTime());
                updateTime = simpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            helper.setText(R.id.dl_tv_date, updateTime);
        }
    }

    public boolean isIs326() {
        return is326;
    }

    public void setIs326(boolean is326) {
        this.is326 = is326;
    }
}
