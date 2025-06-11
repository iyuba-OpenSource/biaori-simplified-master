package com.iyuba.biaori.simplified.ui.break_through;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.break_through.WordAnswerAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityWordListenBinding;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.entity.WordQuestion;
import com.iyuba.biaori.simplified.presenter.break_through.WordExercisePresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.popup.AnalysisPopup;
import com.iyuba.biaori.simplified.view.break_through.WordAnswerContract;
import com.iyuba.biaori.simplified.view.break_through.WordExerciseContract;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 单词训练页面
 */
public class WordListenActivity extends BaseActivity<WordExerciseContract.WordAnswerView, WordExerciseContract.WordAnswerPresenter>
        implements WordExerciseContract.WordAnswerView {

    private ActivityWordListenBinding binding;

    private List<JpWord> jpConceptWordList;

    private List<WordQuestion> wordQuestions;

    private Random random;
    private int position;

    private WordAnswerAdapter answerAdapter;

    private AnalysisPopup analysisPopup;

    private DecimalFormat decimalFormat;

    private LineItemDecoration lineItemDecoration;

    /**
     * type
     * 0：中文
     * 1：英文
     */
    private int questionType = 0;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = 0;
        random = new Random();
        decimalFormat = new DecimalFormat("##.0");
        getBundle();

        binding.toolbar.toolbarIvRight.setVisibility(View.GONE);

        initOperation();
        initData();

        EventBus.getDefault().post(new MediaPauseEventbus());

    }

    @Override
    public View initLayout() {

        binding = ActivityWordListenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public WordExerciseContract.WordAnswerPresenter initPresenter() {
        return new WordExercisePresenter();
    }

    private void initOperation() {

        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        binding.toolbar.toolbarIvTitle.setText("听力训练");
        binding.toolbar.toolbarIvRight.setVisibility(View.INVISIBLE);

        lineItemDecoration = new LineItemDecoration(WordListenActivity.this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.space_10dp));

        if (binding.waRvAnswers.getItemDecorationCount() == 0) {

            binding.waRvAnswers.addItemDecoration(lineItemDecoration);
        }

        binding.waRvAnswers.setLayoutManager(new LinearLayoutManager(this));
        answerAdapter = new WordAnswerAdapter(R.layout.item_answer, new ArrayList<>());
        binding.waRvAnswers.setAdapter(answerAdapter);
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int listPosition) {

                //选择了就不能再选了
                if (answerAdapter.getChoosePosition() != -1 && answerAdapter.gettPosition() != -1) {

                    return;
                }

                WordQuestion wordQuestion = wordQuestions.get(position);
                wordQuestion.setChoosePosition(listPosition);//记录选择的位置
                wordQuestion.setTestTime(getCurrentTime());//记录作答的时间
                answerAdapter.setChoosePosition(listPosition);//适配器记录选择的位置
                answerAdapter.settPosition(wordQuestion.gettPosition());//适配器记录正确答案的位置
                answerAdapter.notifyDataSetChanged();

                //显示下一个按钮
                binding.waLlButton.setVisibility(View.VISIBLE);
                JpWord conceptWord = wordQuestion.getWord();
                if (listPosition == wordQuestion.gettPosition()) {//选中的是正确的

                    conceptWord.setAnswer_status(1);
                } else {

                    conceptWord.setAnswer_status(2);
                }
            }
        });
        //下一个
        binding.waButNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == (wordQuestions.size() - 1)) {

                    showResultAlert();
                    return;
                }

                position++;
                //设置答题开始时间
//                wordQuestions.get(position).setBeginTime(getCurrentTime());

//                binding.toolbar.toolbarIvTitle.setText((position + 1) + "/" + jpConceptWordList.size());
                answerAdapter.settPosition(-1);
                answerAdapter.setChoosePosition(-1);
                showData();
            }
        });
        binding.waButAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initAnalysisPopup(wordQuestions.get(position).getWord());
            }
        });
        binding.wlIvAudio.setOnClickListener(v -> {

            WordQuestion wordQuestion = wordQuestions.get(position);
            JpWord word = wordQuestion.getWord();

            String urlStr;
            if (word.getSource() == null || word.getSource().equals("null")) {//日语n1词库

                urlStr = Constant.JAPAN_AUDIO_BASE_URL_20210623 + "10000/word/" + word.getSound().split("_")[0]
                        + "/"
                        + word.getSound() + ".mp3";
            } else {

                urlStr = Constant.JAPAN_AUDIO_BASE_URL_20210623 + word.getSource() + "/word/" + word.getSound().split("_")[0]
                        + "/"
                        + word.getSound() + ".mp3";
            }
            initMediaPlayer(urlStr);
        });
    }


    /**
     * 播放音频
     *
     * @param path
     */
    private void initMediaPlayer(String path) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    mp.start();
                }
            });
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.prepareAsync();
    }


    /**
     * 检测闯关是否失败
     */
    private void showResultAlert() {

        int totalDoQuestion = 0;//共做多少道题
        int tNUM = 0;//已做题正确数量
        int fNum = 0;//已做题错误数量

        for (int i = 0; i < wordQuestions.size(); i++) {

            WordQuestion wq = wordQuestions.get(i);
            if (wq.getChoosePosition() == -1) {
                break;
            } else {
                totalDoQuestion++;
                if (wq.gettPosition() == wq.getChoosePosition()) {
                    tNUM++;
                } else {
                    fNum++;
                }
            }
        }
//        double fPercentage = 100.0 * fNum / wordQuestions.size();//总的错误率
//        double questionPercentage = 100.0 * totalDoQuestion / wordQuestions.size();//总做题进度

        double tPercentage = 100.0 * tNUM / totalDoQuestion;

        AlertDialog alertDialog = new AlertDialog.Builder(WordListenActivity.this)
                .setTitle("训练结果")
                .setMessage("共做：" + totalDoQuestion + "题，\n做对：" + tNUM + "题，\n正确比例" + decimalFormat.format(tPercentage) + "%")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }


    /**
     * 解释、解析弹窗
     *
     * @param conceptWord
     */
    private void initAnalysisPopup(JpWord conceptWord) {

        if (analysisPopup == null) {

            analysisPopup = new AnalysisPopup(this);
        }

        analysisPopup.setJpWord(conceptWord);
        analysisPopup.showPopupWindow();
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrTime = formatter.format(curDate);
        return strCurrTime;
    }

    private void initData() {

        new Thread(() -> {

            wordQuestions = new ArrayList<>();
            for (int i = 0; i < jpConceptWordList.size(); i++) {

                JpWord conceptWord = jpConceptWordList.get(i);
                WordQuestion wordQuestion = new WordQuestion();
                wordQuestion.setWord(conceptWord);
                List<JpWord> addConceptWord = getRandom3(conceptWord);
                wordQuestion.setAnswerList(addConceptWord);
                int tPosition = random.nextInt(4);
                wordQuestion.getAnswerList().add(tPosition, conceptWord);
                wordQuestion.settPosition(tPosition);
                wordQuestion.setType(questionType);
                wordQuestions.add(wordQuestion);
            }

            runOnUiThread(this::showData);
            //设置开始时间
//            wordQuestions.get(0).setBeginTime(getCurrentTime());
        }).start();
    }


    public List<JpWord> getRandom3(JpWord conceptWord) {

        List<JpWord> jpWordList = new ArrayList<>();
        String sqlSting = "SELECT * FROM JpWord WHERE level = %d and id != %d ORDER BY RANDOM() limit 3";
        String execSql = String.format(sqlSting, Integer.parseInt(conceptWord.getLevel()), conceptWord.getId());
        Cursor cursor = LitePal.findBySQL(execSql);
        while (cursor.moveToNext()) {

            JpWord jpWord = new JpWord();
            jpWord.setId(cursor.getInt(cursor.getColumnIndexOrThrow("wordid")));
            jpWord.setSound(cursor.getString(cursor.getColumnIndexOrThrow("sound")));
            jpWord.setWord(cursor.getString(cursor.getColumnIndexOrThrow("word")));
            jpWord.setWordCh(cursor.getString(cursor.getColumnIndexOrThrow("wordch")));
            jpWord.setPron(cursor.getString(cursor.getColumnIndexOrThrow("pron")));
            jpWord.setSentence(cursor.getString(cursor.getColumnIndexOrThrow("sentence")));
            jpWord.setSentenceCh(cursor.getString(cursor.getColumnIndexOrThrow("sentencech")));
            jpWord.setSpeech(cursor.getString(cursor.getColumnIndexOrThrow("speech")));
            jpWord.setWordNum(cursor.getString(cursor.getColumnIndexOrThrow("wordnum")));
            jpWord.setSource(cursor.getString(cursor.getColumnIndexOrThrow("source")));
            jpWord.setSourceid(cursor.getString(cursor.getColumnIndexOrThrow("sourceid")));
            jpWord.setIdindex(cursor.getString(cursor.getColumnIndexOrThrow("idindex")));
            jpWord.setLevel(cursor.getString(cursor.getColumnIndexOrThrow("level")));
            jpWordList.add(jpWord);
        }
        cursor.close();
        return jpWordList;
    }

    private void showData() {

        //index
        binding.wlPbIndex.setProgress((position + 1));
        binding.wlTvIndex.setText((position + 1) + "/" + jpConceptWordList.size());
        //隐藏按钮
        binding.waLlButton.setVisibility(View.INVISIBLE);

        WordQuestion wordQuestion = wordQuestions.get(position);
        answerAdapter.setType(wordQuestion.getType());
        answerAdapter.setNewData(wordQuestion.getAnswerList());

        JpWord word = wordQuestion.getWord();

        String urlStr;
        if (word.getSource() == null || word.getSource().equals("null")) {//日语n1词库

            urlStr = Constant.JAPAN_AUDIO_BASE_URL_20210623 + "10000/word/" + word.getSound().split("_")[0]
                    + "/"
                    + word.getSound() + ".mp3";
        } else {

            urlStr = Constant.JAPAN_AUDIO_BASE_URL_20210623 + word.getSource() + "/word/" + word.getSound().split("_")[0]
                    + "/"
                    + word.getSound() + ".mp3";
        }
        initMediaPlayer(urlStr);
        //设置
        if ((position + 1) == jpConceptWordList.size()) {

            binding.waButNext.setText("完成");
        }
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            jpConceptWordList = (List<JpWord>) bundle.getSerializable("DATAS");
            questionType = bundle.getInt("QUESTION_TYPE");
        }
    }

    public static void startActivity(Activity activity, List<JpWord> jpConceptWordList, int questionType) {

        Intent intent = new Intent(activity, WordListenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATAS", (Serializable) jpConceptWordList);
        bundle.putInt("QUESTION_TYPE", questionType);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {

            mediaPlayer.release();
        }
    }
}