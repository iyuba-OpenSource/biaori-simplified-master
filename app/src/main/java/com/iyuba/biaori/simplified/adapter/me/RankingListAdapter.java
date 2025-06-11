package com.iyuba.biaori.simplified.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingListAdapter extends BaseQuickAdapter<StudyRankingBean.DataDTO, BaseViewHolder> {

    private String kind = "speak";
    public static String KINDS[] = new String[]{"speak", "listen", "study", "test"};

    private DecimalFormat decimalFormat;

    public RankingListAdapter(int layoutResId, @Nullable List<StudyRankingBean.DataDTO> data) {
        super(layoutResId, data);

        decimalFormat = new DecimalFormat("#00.0");
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, StudyRankingBean.DataDTO dataDTO) {


        baseViewHolder.setText(R.id.ranking_tv_num, dataDTO.getRanking() + "");
        if (kind.equals(KINDS[0])) {//口语

            CircleImageView ranking_civ_portrait = baseViewHolder.getView(R.id.ranking_civ_portrait);
            Glide.with(baseViewHolder.itemView.getContext()).load(dataDTO.getImgSrc()).into(ranking_civ_portrait);
            //名称
            baseViewHolder.setText(R.id.ranking_tv_l1, dataDTO.getName());
            //合成配音数：
            baseViewHolder.setText(R.id.ranking_tv_l2, "合成配音数：" + dataDTO.getCount() + "");
            //总分
            baseViewHolder.setText(R.id.ranking_tv_r1, "总分：" + dataDTO.getScores() + "");
            //平均分
            baseViewHolder.setText(R.id.ranking_tv_r2, "平均分：" + dataDTO.getScores() / dataDTO.getCount() + "");
        } else if (kind.equals(KINDS[1])) {//听力

            CircleImageView ranking_civ_portrait = baseViewHolder.getView(R.id.ranking_civ_portrait);
            Glide.with(baseViewHolder.itemView.getContext()).load(dataDTO.getImgSrc()).into(ranking_civ_portrait);
            //名称
            baseViewHolder.setText(R.id.ranking_tv_l1, dataDTO.getName());
            //时间
            int m = dataDTO.getTotalTime() / 60;
            int h = m / 60;
            m = m % 60;
            int s = dataDTO.getTotalTime() % 60;
            if (h == 0) {

                baseViewHolder.setText(R.id.ranking_tv_l2, m + "分" + s + "秒");
            } else {

                baseViewHolder.setText(R.id.ranking_tv_l2, h + "小时" + m + "分" + s + "秒");
            }

            //文章数
            baseViewHolder.setText(R.id.ranking_tv_r1, "文章数：" + dataDTO.getTotalEssay() + "");
            //单词数：
            baseViewHolder.setText(R.id.ranking_tv_r2, "单词数：" + dataDTO.getTotalWord() + "");
        } else if (kind.equals(KINDS[2])) {//学习

            CircleImageView ranking_civ_portrait = baseViewHolder.getView(R.id.ranking_civ_portrait);
            Glide.with(baseViewHolder.itemView.getContext()).load(dataDTO.getImgSrc()).into(ranking_civ_portrait);
            //名称
            baseViewHolder.setText(R.id.ranking_tv_l1, dataDTO.getName());
            //时间
            int m = dataDTO.getTotalTime() / 60;
            int h = m / 60;
            int s = dataDTO.getTotalTime() % 60;
            baseViewHolder.setText(R.id.ranking_tv_l2, h + "小时" + m + "分" + s + "秒");
            //文章数
            baseViewHolder.setText(R.id.ranking_tv_r1, "文章数：" + dataDTO.getTotalEssay() + "");
            //单词数：
            baseViewHolder.setText(R.id.ranking_tv_r2, "单词数：" + dataDTO.getTotalWord() + "");

        } else if (kind.equals(KINDS[3])) {//测试（练习题）

            CircleImageView ranking_civ_portrait = baseViewHolder.getView(R.id.ranking_civ_portrait);
            Glide.with(baseViewHolder.itemView.getContext()).load(dataDTO.getImgSrc()).into(ranking_civ_portrait);
            //名称
            baseViewHolder.setText(R.id.ranking_tv_l1, dataDTO.getName());
            //总体数
            baseViewHolder.setText(R.id.ranking_tv_l2, "总体数:" + dataDTO.getTotalTest());
            //正确数
            baseViewHolder.setText(R.id.ranking_tv_r1, "正确数：" + dataDTO.getTotalRight());
            //正确率：
            double pf = 100.0 * dataDTO.getTotalRight() / dataDTO.getTotalTest();
            String p = decimalFormat.format(pf);
            baseViewHolder.setText(R.id.ranking_tv_r2, "正确率：" + p + "%");
        }
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
