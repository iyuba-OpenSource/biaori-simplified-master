package com.iyuba.biaori.simplified.adapter.vip;

import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.entity.VipKind;

import java.util.List;


/**
 * 会员价格对应adapter
 */
public class VipKindAdapter extends BaseQuickAdapter<VipKind.Kind, BaseViewHolder> {

    //位置
    private int postion = 0;

    public VipKindAdapter(int layoutResId, @Nullable List<VipKind.Kind> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, VipKind.Kind kind) {

        RadioButton vip_rb = baseViewHolder.getView(R.id.vip_rb);
        if (baseViewHolder.getAdapterPosition() == postion) {

            vip_rb.setChecked(true);
        } else {

            vip_rb.setChecked(false);
        }
        baseViewHolder.setText(R.id.vip_desc, kind.getText());
        baseViewHolder.setText(R.id.vip_price, "￥" + kind.getPrice());
    }


    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
        notifyDataSetChanged();
    }
}
