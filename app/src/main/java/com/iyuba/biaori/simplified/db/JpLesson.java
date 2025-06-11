package com.iyuba.biaori.simplified.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 存储课程的表
 */
public class JpLesson extends LitePalSupport implements Serializable {


    @SerializedName("lesson")
    private String lesson;
    @SerializedName("lessonCH")
    private String lessonCH;
    @SerializedName("lessonID")
    @Column(nullable = false)
    private String lessonID;
    @SerializedName("source")
    private String source;
    @SerializedName("progressListen")
    private Object progressListen;
    @SerializedName("version")
    private String version;
    @SerializedName("sentenceNum")
    private String sentenceNum;
    @SerializedName("wordNum")
    private String wordNum;

    /**
     * 是否存储 0 没收藏，1已收藏
     */
    @Column(defaultValue = "0")
    private int collect;

    /**
     * 下载标志
     * 1  下载   0  未下载
     */
    @Column(defaultValue = "0")
    private int downloadFlag = 0;

    /**
     * 音频地址
     */
    private String sound;

    /**
     * 记录读到第几句
     */
    @Column(defaultValue = "0")
    private int TestNumber = 0;


    /**
     * 记录结束的时间
     * 用来同步数据时，更新最新的数据
     */
    private String endTime;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTestNumber() {
        return TestNumber;
    }

    public void setTestNumber(int testNumber) {
        TestNumber = testNumber;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getDownloadFlag() {
        return downloadFlag;
    }

    public void setDownloadFlag(int downloadFlag) {
        this.downloadFlag = downloadFlag;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getLessonCH() {
        return lessonCH;
    }

    public void setLessonCH(String lessonCH) {
        this.lessonCH = lessonCH;
    }

    public String getLessonID() {
        return lessonID;
    }

    public void setLessonID(String lessonID) {
        this.lessonID = lessonID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Object getProgressListen() {
        return progressListen;
    }

    public void setProgressListen(Object progressListen) {
        this.progressListen = progressListen;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSentenceNum() {
        return sentenceNum;
    }

    public void setSentenceNum(String sentenceNum) {
        this.sentenceNum = sentenceNum;
    }

    public String getWordNum() {
        return wordNum;
    }

    public void setWordNum(String wordNum) {
        this.wordNum = wordNum;
    }
}
