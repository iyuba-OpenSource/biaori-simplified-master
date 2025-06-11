package com.iyuba.biaori.simplified.adapter.textbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.entity.Para;

import java.util.List;

public class ParaAdapter extends BaseQuickAdapter<Para, BaseViewHolder> {

    private boolean isShowEn = false;

    public ParaAdapter(int layoutResId, @Nullable List<Para> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Para item) {

        helper.setText(R.id.para_tv_en, item.getEn());
        helper.setText(R.id.para_tv_cn, item.getCn());
        if (isShowEn) {

            helper.setGone(R.id.para_tv_cn, true);
        } else {

            helper.setGone(R.id.para_tv_cn, false);
        }
    }


    public boolean isShowEn() {
        return isShowEn;
    }

    public void setShowEn(boolean showEn) {
        isShowEn = showEn;
    }
}
