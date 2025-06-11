package com.iyuba.biaori.simplified.model.bean.me;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudyRankingBean {


    @SerializedName("result")
    private int result;
    @SerializedName("myimgSrc")
    private String myimgSrc;
    @SerializedName("myid")
    private int myid;
    @SerializedName("myranking")
    private int myranking;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("totalTime")
    private int totalTime;
    @SerializedName("totalWord")
    private int totalWord;
    @SerializedName("myname")
    private String myname;
    @SerializedName("totalEssay")
    private int totalEssay;
    @SerializedName("message")
    private String message;
    @SerializedName("myscores")
    private int myscores;
    @SerializedName("mycount")
    private int mycount;
    @SerializedName("vip")
    private String vip;
    @SerializedName("totalRight")
    private int totalRight;
    @SerializedName("totalTest")
    private int totalTest;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMyimgSrc() {
        return myimgSrc;
    }

    public void setMyimgSrc(String myimgSrc) {
        this.myimgSrc = myimgSrc;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getMyranking() {
        return myranking;
    }

    public void setMyranking(int myranking) {
        this.myranking = myranking;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(int totalWord) {
        this.totalWord = totalWord;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public int getTotalEssay() {
        return totalEssay;
    }

    public void setTotalEssay(int totalEssay) {
        this.totalEssay = totalEssay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMyscores() {
        return myscores;
    }

    public void setMyscores(int myscores) {
        this.myscores = myscores;
    }

    public int getMycount() {
        return mycount;
    }

    public void setMycount(int mycount) {
        this.mycount = mycount;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public int getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(int totalRight) {
        this.totalRight = totalRight;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
    }

    public static class DataDTO {
        @SerializedName("uid")
        private int uid;
        @SerializedName("totalTime")
        private int totalTime;
        @SerializedName("totalWord")
        private int totalWord;
        @SerializedName("name")
        private String name;
        @SerializedName("ranking")
        private int ranking;
        @SerializedName("sort")
        private int sort;
        @SerializedName("totalEssay")
        private int totalEssay;
        @SerializedName("imgSrc")
        private String imgSrc;
        @SerializedName("scores")
        private int scores;
        @SerializedName("count")
        private int count;
        @SerializedName("vip")
        private String vip;
        @SerializedName("totalRight")
        private int totalRight;
        @SerializedName("totalTest")
        private int totalTest;


        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        public int getTotalWord() {
            return totalWord;
        }

        public void setTotalWord(int totalWord) {
            this.totalWord = totalWord;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getTotalEssay() {
            return totalEssay;
        }

        public void setTotalEssay(int totalEssay) {
            this.totalEssay = totalEssay;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public int getTotalRight() {
            return totalRight;
        }

        public void setTotalRight(int totalRight) {
            this.totalRight = totalRight;
        }

        public int getTotalTest() {
            return totalTest;
        }

        public void setTotalTest(int totalTest) {
            this.totalTest = totalTest;
        }
    }
}
