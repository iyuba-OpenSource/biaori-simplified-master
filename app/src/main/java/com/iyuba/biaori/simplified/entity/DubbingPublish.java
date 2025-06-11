package com.iyuba.biaori.simplified.entity;

import java.util.List;

public class DubbingPublish {


    private String appName;

    private int flag;

    private String format;
    private int idIndex;

    private int paraId;

    private String platform;

    private int score;

    private int shuoshuotype;

    private String sound;

    private String topic;

    private String username;

    private int voaid;

    private List<WavList> wavList;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getParaId() {
        return paraId;
    }

    public void setParaId(int paraId) {
        this.paraId = paraId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShuoshuotype() {
        return shuoshuotype;
    }

    public void setShuoshuotype(int shuoshuotype) {
        this.shuoshuotype = shuoshuotype;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVoaid() {
        return voaid;
    }

    public void setVoaid(int voaid) {
        this.voaid = voaid;
    }

    public List<WavList> getWavList() {
        return wavList;
    }

    public void setWavList(List<WavList> wavList) {
        this.wavList = wavList;
    }

    public static class WavList {

        private String URL;

        private String beginTime;

        private String endTime;

        private String duration;

        private int index;

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
