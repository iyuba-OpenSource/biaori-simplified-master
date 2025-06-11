package com.iyuba.biaori.simplified.model.bean.me;

import com.google.gson.annotations.SerializedName;

public class MyTimeBean {


    @SerializedName("result")
    private String result;
    @SerializedName("totalWord")
    private String totalWord;
    @SerializedName("totalTime")
    private String totalTime;
    @SerializedName("totalUser")
    private String totalUser;
    @SerializedName("ranking")
    private String ranking;
    @SerializedName("totalDaysTime")
    private String totalDaysTime;
    @SerializedName("totalWords")
    private String totalWords;
    @SerializedName("totalDays")
    private String totalDays;
    @SerializedName("sentence")
    private String sentence;
    @SerializedName("shareId")
    private String shareId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(String totalWord) {
        this.totalWord = totalWord;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(String totalUser) {
        this.totalUser = totalUser;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTotalDaysTime() {
        return totalDaysTime;
    }

    public void setTotalDaysTime(String totalDaysTime) {
        this.totalDaysTime = totalDaysTime;
    }

    public String getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(String totalWords) {
        this.totalWords = totalWords;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
}
