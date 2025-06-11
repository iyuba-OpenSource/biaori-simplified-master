package com.iyuba.biaori.simplified.db;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class JpWord extends LitePalSupport implements Serializable {


    @SerializedName("id")
    @Column(unique = true, index = true)
    private int wordId;
    @SerializedName("sound")
    private String sound;
    @SerializedName("word")
    private String word;
    @SerializedName("word_ch")
    private String wordCh;
    @SerializedName("pron")
    private String pron;
    @SerializedName("sentence")
    private String sentence;
    @SerializedName("sentence_ch")
    private String sentenceCh;
    @SerializedName("speech")
    private String speech;
    @SerializedName("wordNum")
    private String wordNum;
    @SerializedName("source")
    private String source;
    @SerializedName("sourceid")
    private String sourceid;
    @SerializedName("idindex")
    private String idindex;
    @SerializedName("level")
    @Column(index = true)
    private String level;

    /**
     * 评测后得到的链接
     */
    @Column(ignore = true)
    private String url;
    /**
     * 得分
     */
    @Column(ignore = true)
    private String total_score;

    /**
     * 答题状态
     * 0：未答题
     * 1：回答正确
     * 2：回答错误
     */
    @Column(defaultValue = "0")
    private int answer_status;


    /**
     * 是否显示中文
     */
    @Column(ignore = true)
    private boolean isShow = false;

    /**
     * 0:未收藏
     * 1：已收藏
     */
    @Column(defaultValue = "0")
    private int collect = 0;

    /**
     * 单词收藏
     */
    private String sentenceSound;


    public String getSentenceSound() {
        return sentenceSound;
    }

    public void setSentenceSound(String sentenceSound) {
        this.sentenceSound = sentenceSound;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(int answer_status) {
        this.answer_status = answer_status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public int getId() {
        return wordId;
    }

    public void setId(int wordId) {
        this.wordId = wordId;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordCh() {
        return wordCh;
    }

    public void setWordCh(String wordCh) {
        this.wordCh = wordCh;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceCh() {
        return sentenceCh;
    }

    public void setSentenceCh(String sentenceCh) {
        this.sentenceCh = sentenceCh;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getWordNum() {
        return wordNum;
    }

    public void setWordNum(String wordNum) {
        this.wordNum = wordNum;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getIdindex() {
        return idindex;
    }

    public void setIdindex(String idindex) {
        this.idindex = idindex;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}