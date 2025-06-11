package com.iyuba.biaori.simplified.ui.break_through;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.break_through.CPWordAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityBtwordsBinding;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.ui.textbook.WordDetailsActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 单词闯关单词列表
 */
public class BTWordsActivity extends AppCompatActivity {


    private ActivityBtwordsBinding activityBtwordsBinding;

    /**
     * 位置等同关数
     */
    private int position;

    private SharedPreferences sp;
    //单词个数
    private int wordNum = 30;
    //单词level
    private int level;

    private CPWordAdapter cpWordAdapter;

    private LineItemDecoration lineItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBtwordsBinding = ActivityBtwordsBinding.inflate(getLayoutInflater());
        setContentView(activityBtwordsBinding.getRoot());
        getBundle();

        initOperation();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    private void initOperation() {


        activityBtwordsBinding.btwordsButJp2cn.setOnClickListener(v -> {

            WordExerciseActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), 0);
        });
        activityBtwordsBinding.btwordsButCn2jp.setOnClickListener(v -> {

            WordExerciseActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), 1);
        });
        activityBtwordsBinding.btwordsButListen.setOnClickListener(v -> {

            WordListenActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), 0);
        });


        activityBtwordsBinding.toolbar.toolbarIvTitle.setText("第" + (position + 1) + "关单词");
        activityBtwordsBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //开始学习
        activityBtwordsBinding.btwordsButStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WordDetailsActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), 0);
            }
        });
        //开始闯关
        activityBtwordsBinding.btwordsButBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WordAnswerActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData());
            }
        });


        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_line_word));
        activityBtwordsBinding.btwordsRvList.addItemDecoration(lineItemDecoration);

        activityBtwordsBinding.btwordsRvList.setLayoutManager(new LinearLayoutManager(this));
        cpWordAdapter = new CPWordAdapter(R.layout.item_cp_word, new ArrayList<>());
        activityBtwordsBinding.btwordsRvList.setAdapter(cpWordAdapter);

        cpWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                JpWord word = cpWordAdapter.getItem(position);
                if (word.isShow()) {

                    word.setShow(false);
                } else {

                    word.setShow(true);
                }
                cpWordAdapter.notifyItemChanged(position);
            }
        });
        cpWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                WordDetailsActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), position);
            }
        });
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            position = bundle.getInt("POSITION", 0);
            level = bundle.getInt("LEVEL", 6);
        }
        sp = getSharedPreferences(Constant.SP_BREAK_THROUGH, MODE_PRIVATE);
        wordNum = sp.getInt(Constant.SP_KEY_WORD_NUM, 30);
    }

    private void initData() {


        List<JpWord> jpWordList = LitePal
                .where("level = ? ", level + "")
                .order("wordid")
                .limit(wordNum)
                .offset(position * wordNum)
                .find(JpWord.class);

        cpWordAdapter.setNewData(jpWordList);

    }

    public static void startActivity(Activity activity, int position, int level) {

        Intent intent = new Intent(activity, BTWordsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putInt("LEVEL", level);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}