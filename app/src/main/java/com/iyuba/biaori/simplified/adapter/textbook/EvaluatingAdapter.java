package com.iyuba.biaori.simplified.adapter.textbook;

import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.widget.rpb.RoundProgressBar;

import java.util.List;

import retrofit2.http.HEAD;

public class EvaluatingAdapter extends BaseQuickAdapter<Sentence, BaseViewHolder> {

    private boolean isPlay = false;

    private boolean isRecord = false;

    //分贝
    private double db;

    /**
     * 正在操作的数据位置
     */
    private int position = -1;

    /**
     * 1:播放原音
     * 2:播放录音
     */
    private int flag = 1;



    public EvaluatingAdapter(int layoutResId, @Nullable List<Sentence> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sentence sentence) {

        helper.addOnClickListener(R.id.evaluating_iv_play);
        helper.addOnClickListener(R.id.evaluating_rpb_record);
        helper.addOnClickListener(R.id.evaluating_iv_play_oneself);
        helper.addOnClickListener(R.id.evaluating_iv_submit);
        helper.addOnClickListener(R.id.evaluating_iv_share);
        //句子
        helper.setText(R.id.evaluating_tv_text, sentence.getSentence());
        helper.setText(R.id.evaluating_tv_ch, sentence.getSentencech().trim());
        //设置录音按钮
        RoundProgressBar evaluating_rpb_record = helper.getView(R.id.evaluating_rpb_record);
        if (isRecord && position == helper.getBindingAdapterPosition()) {

            evaluating_rpb_record.setProgress((int) db);
        } else {

            evaluating_rpb_record.setProgress(0);
        }
        ImageView evaluating_iv_play = helper.getView(R.id.evaluating_iv_play);
        ImageView evaluating_iv_play_oneself = helper.getView(R.id.evaluating_iv_play_oneself);
        //设置播放按钮的状态
        if (isPlay && position == helper.getBindingAdapterPosition()) {

            if (flag == 1) {//播放原音

                evaluating_iv_play.setImageResource(R.mipmap.icon_pause_ori);
            } else {//播放录音

                evaluating_iv_play_oneself.setImageResource(R.mipmap.icon_pause_record);
            }

        } else {

            if (flag == 1) {//播放原音

                evaluating_iv_play.setImageResource(R.mipmap.icon_play_ori);
            } else {//播放录音

                evaluating_iv_play_oneself.setImageResource(R.mipmap.icon_play_record);
            }
        }
        //得分
        if (sentence.getScore() == null) {

            helper.setGone(R.id.evaluating_tv_source, false);
            helper.setGone(R.id.evaluating_iv_play_oneself, false);
            helper.setGone(R.id.evaluating_iv_submit, false);
            helper.setGone(R.id.evaluating_iv_share, false);
        } else {

            helper.setGone(R.id.evaluating_tv_source, true);
            helper.setGone(R.id.evaluating_iv_play_oneself, true);
            helper.setGone(R.id.evaluating_iv_submit, true);
            helper.setGone(R.id.evaluating_iv_share, true);
            helper.setText(R.id.evaluating_tv_source, (int) (Double.parseDouble(sentence.getScore()) * 20) + "");
        }
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    public double getDb() {
        return db;
    }

    public void setDb(double db) {
        this.db = db;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
