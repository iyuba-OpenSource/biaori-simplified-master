package com.iyuba.biaori.simplified.adapter.vip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.entity.VipKind;

import java.util.List;

/**
 * vip中的vip类型
 */
public class VipAdapter extends BaseQuickAdapter<VipKind, BaseViewHolder> {

    /**
     * 选择位置
     */
    private int position = 0;

    public VipAdapter(int layoutResId, @Nullable List<VipKind> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, VipKind vipKind) {

        baseViewHolder.setImageResource(R.id.vipkind_iv_icon, vipKind.getIcon());

        if (baseViewHolder.getAdapterPosition() == position) {

            baseViewHolder.setBackgroundRes(R.id.vip_ly_cate, R.color.shallow_orange);
        } else {

            baseViewHolder.setBackgroundRes(R.id.vip_ly_cate, android.R.color.white);
        }

        if (vipKind.getName() == null) {

            baseViewHolder.setVisible(R.id.vipkind_tc_name, false);

        } else {

            baseViewHolder.setVisible(R.id.vipkind_tc_name, true);
            baseViewHolder.setText(R.id.vipkind_tc_name, vipKind.getName());
        }

        if (vipKind.getName2() == null) {

            baseViewHolder.setVisible(R.id.vipkind_tc_name2, false);
        } else {

            baseViewHolder.setVisible(R.id.vipkind_tc_name2, true);
            baseViewHolder.setText(R.id.vipkind_tc_name2, vipKind.getName2());
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }
}
