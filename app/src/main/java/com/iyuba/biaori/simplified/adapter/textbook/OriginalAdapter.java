package com.iyuba.biaori.simplified.adapter.textbook;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;

import java.util.List;

/**
 * 原文fragment
 */
public class OriginalAdapter extends BaseQuickAdapter<Sentence, BaseViewHolder> {

    private int position = 0;

    private int gColor;

    private int blackColor = 0;

    private int grayColor = 0;

    public OriginalAdapter(int layoutResId, @Nullable List<Sentence> data) {
        super(layoutResId, data);
        gColor = Color.parseColor("#00a490");
        blackColor = Color.BLACK;
        grayColor = ResourcesCompat.getColor(MyApplication.getContext().getResources(), android.R.color.darker_gray, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sentence item) {

        //甲：
        if (item.getAnnouncer() == null) {

            helper.setText(R.id.original_tv_announcer, "");
        } else {

            helper.setText(R.id.original_tv_announcer, item.getAnnouncer() + ":");
        }

        helper.setText(R.id.original_tv_jp, item.getSentence());

        helper.setText(R.id.original_tv_ch, item.getSentencech());

        if (helper.getBindingAdapterPosition() == position) {//当前在读的位置

            helper.setTextColor(R.id.original_tv_jp, gColor);

            helper.setTextColor(R.id.original_tv_ch, gColor);
        } else {

            helper.setTextColor(R.id.original_tv_jp, blackColor);

            helper.setTextColor(R.id.original_tv_ch, grayColor);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
