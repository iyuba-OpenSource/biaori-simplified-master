package com.iyuba.biaori.simplified.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Sentence extends LitePalSupport implements Serializable {

    @SerializedName("announcer")
    private String announcer;
    @SerializedName("sentence")
    private String sentence;
    @SerializedName("sentencech")
    private String sentencech;
    @SerializedName("source")
    private String source;
    @SerializedName("sourceid")
    private String sourceid;
    @SerializedName("idindex")
    private String idindex;
    @SerializedName("sound")
    private String sound;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;

    /**
     * 得分
     */
    private String score;

    /**
     * 录音的地址
     */
    private String recordSoundUrl;


    /**
     * 用来分享
     */
    private String shuoshuoId;


    public String getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(String shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRecordSoundUrl() {
        return recordSoundUrl;
    }

    public void setRecordSoundUrl(String recordSoundUrl) {
        this.recordSoundUrl = recordSoundUrl;
    }

    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentencech() {
        return sentencech;
    }

    public void setSentencech(String sentencech) {
        this.sentencech = sentencech;
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
