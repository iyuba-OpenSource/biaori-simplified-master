package com.iyuba.biaori.simplified.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.JpLesson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDownloadAdapter extends BaseQuickAdapter<JpLesson, BaseViewHolder> {

    private int[] pics = new int[]{R.mipmap.bg_test1, R.mipmap.bg_test2, R.mipmap.bg_test3, R.mipmap.bg_test4,
            R.mipmap.bg_test5, R.mipmap.bg_test10, R.mipmap.bg_test13};

    public MyDownloadAdapter(int layoutResId, @Nullable List<JpLesson> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, JpLesson item) {

        CircleImageView textbook_iv_lesson = helper.getView(R.id.textbook_iv_lesson);
        Glide.with(helper.itemView.getContext()).load(pics[helper.getBindingAdapterPosition() % pics.length]).into(textbook_iv_lesson);

        //第几课
        String[] title = item.getLesson().split("\\s+");
        helper.setText(R.id.textbook_tv_number, title[0]);

        //日文标题
        helper.setText(R.id.textbook_tv_lesson, title[1]);
        //中文标题
        helper.setText(R.id.textbook_tv_lessonch, item.getLessonCH());
        //删除
        helper.addOnClickListener(R.id.td_tv_del);
    }
}
