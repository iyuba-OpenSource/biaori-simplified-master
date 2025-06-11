package com.iyuba.biaori.simplified.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.widget.rpb.RoundProgressBar;

import org.litepal.LitePal;

import java.util.List;

/**
 * 课程列表
 */
public class TextbookAdapter extends BaseQuickAdapter<JpLesson, TextbookAdapter.TextbookViewHolder> {

    private int[] pics = new int[]{R.mipmap.bg_test1, R.mipmap.bg_test2, R.mipmap.bg_test3, R.mipmap.bg_test4,
            R.mipmap.bg_test5, R.mipmap.bg_test10, R.mipmap.bg_test13};

    public TextbookAdapter(int layoutResId, @Nullable List<JpLesson> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull TextbookViewHolder helper, JpLesson item) {

        helper.setJpLesson(item);
        ImageView textbook_iv_lesson = helper.getView(R.id.textbook_iv_lesson);
        Glide.with(helper.itemView.getContext()).load(pics[helper.getBindingAdapterPosition() % pics.length]).into(textbook_iv_lesson);

        //第几课
        String[] title = item.getLesson().split("\\s+");
        helper.setText(R.id.textbook_tv_number, title[0]);

        //日文标题
        helper.setText(R.id.textbook_tv_lesson, title[1]);
        //中文标题
        helper.setText(R.id.textbook_tv_lessonch, item.getLessonCH());
        //听力进度
        RoundProgressBar textbook_rpb_listen = helper.getView(R.id.textbook_rpb_listen);
        if (item.getSentenceNum() != null) {//课文收藏中可能为空

            textbook_rpb_listen.setMax(Integer.parseInt(item.getSentenceNum()));
            textbook_rpb_listen.setProgress(item.getTestNumber());

            int tp = (int) (item.getTestNumber() * 100.0 / Integer.parseInt(item.getSentenceNum()));
            if (tp > 0) {

                helper.setBackgroundRes(R.id.textbook_rpb_listen, R.mipmap.icon_listen_green);
            } else {
                helper.setBackgroundRes(R.id.textbook_rpb_listen, R.mipmap.icon_listen);
            }
            helper.setText(R.id.textbook_tv_listen, tp + "%");
        } else {

            helper.setBackgroundRes(R.id.textbook_rpb_listen, R.mipmap.icon_listen);
            helper.setText(R.id.textbook_tv_listen, "0%");
        }


        //单词进度

        if (item.getWordNum() == null) {

            helper.setText(R.id.textbook_tv_word, "-/-");
            helper.setBackgroundRes(R.id.textbook_rpb_word, R.mipmap.icon_word);
        } else {

            String source = item.getSource();
            List<Book> bookList = LitePal.where("source = ?", source).find(Book.class);
            String levelStr = bookList.get(0).getLevel();

            int tCount = 0;
            List<JpWord> jpWordList = LitePal.where("sourceid = ? and level = ?", Integer.parseInt(item.getLessonID()) + "", levelStr).find(JpWord.class);
            for (int i = 0; i < jpWordList.size(); i++) {

                JpWord jpWord = jpWordList.get(i);

                if (jpWord.getAnswer_status() == 1) {

                    tCount++;
                }
            }
            RoundProgressBar textbook_rpb_word = helper.getView(R.id.textbook_rpb_word);
            textbook_rpb_word.setMax(Integer.parseInt(item.getWordNum()));
            textbook_rpb_word.setProgress(tCount);
            helper.setText(R.id.textbook_tv_word, tCount + "/" + item.getWordNum());
            if (tCount > 0) {

                helper.setBackgroundRes(R.id.textbook_rpb_word, R.mipmap.icon_word_green);
            } else {

                helper.setBackgroundRes(R.id.textbook_rpb_word, R.mipmap.icon_word);
            }
        }


        //口语
        if (item.getSentenceNum() == null) {

            helper.setText(R.id.textbook_tv_eval, "-/-");
            helper.setBackgroundRes(R.id.textbook_rpb_eval, R.mipmap.icon_eval);
        } else {

            int count = LitePal.where("sourceid = ? and score is NOT NULL", item.getLessonID()).count(Sentence.class);
            RoundProgressBar textbook_rpb_eval = helper.getView(R.id.textbook_rpb_eval);
            textbook_rpb_eval.setMax(Integer.parseInt(item.getSentenceNum()));
            textbook_rpb_eval.setProgress(count);
            helper.setText(R.id.textbook_tv_eval, count + "/" + item.getSentenceNum());
            if (count > 0) {

                helper.setBackgroundRes(R.id.textbook_rpb_eval, R.mipmap.icon_eval_green);
            } else {

                helper.setBackgroundRes(R.id.textbook_rpb_eval, R.mipmap.icon_eval);
            }
        }
    }

    public static class TextbookViewHolder extends BaseViewHolder {

        private JpLesson jpLesson;

        public TextbookViewHolder(View view) {
            super(view);
        }

        public JpLesson getJpLesson() {
            return jpLesson;
        }

        public void setJpLesson(JpLesson jpLesson) {
            this.jpLesson = jpLesson;
        }
    }
}
