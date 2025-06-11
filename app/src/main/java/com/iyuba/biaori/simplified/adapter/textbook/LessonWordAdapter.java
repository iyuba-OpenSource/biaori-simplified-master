package com.iyuba.biaori.simplified.adapter.textbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;

import java.util.List;

public class LessonWordAdapter extends BaseQuickAdapter<JpWord, BaseViewHolder> {

    public LessonWordAdapter(int layoutResId, @Nullable List<JpWord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, JpWord item) {

        if (item.getPron() == null) {

            helper.setText(R.id.lw_tv_word, item.getWord());
        } else {

            helper.setText(R.id.lw_tv_word, item.getWord() + "[" + item.getPron() + "]");
        }

        helper.setText(R.id.lw_tv_cn, item.getWordCh());
    }
}
