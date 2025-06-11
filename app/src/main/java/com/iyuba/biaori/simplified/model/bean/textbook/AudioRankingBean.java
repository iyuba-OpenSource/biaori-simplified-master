package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AudioRankingBean {

    /**
     * {
     *     "result": 15,
     *     "myimgSrc": "http://static1.iyuba.cn/uc_server/head/2022/11/20/11/10/47/44df29f3-3570-4312-aeff-fdf359d1a799-m.jpg",
     *     "myid": 4729911,
     *     "myranking": 478,
     *     "data": [
     *         {
     *             "uid": 13634244,
     *             "scores": 1768,
     *             "name": "森の猫",
     *             "count": 19,
     *             "ranking": 1,
     *             "sort": 1,
     *             "vip": "0",
     *             "imgSrc": "http://static1.iyuba.cn/uc_server/head/2022/11/28/18/17/30/7c97ceb5-3a75-4e5f-a350-551cbc6a8917-m.jpg"
     *         },
     *         {
     *             "uid": 13653918,
     *             "scores": 1756,
     *             "name": "達也Tatsuya",
     *             "count": 19,
     *             "ranking": 2,
     *             "sort": 2,
     *             "vip": "0",
     *             "imgSrc": "http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"
     *         }
     *     ],
     *     "myname": "JInrong110",
     *     "myscores": 0,
     *     "mycount": 0,
     *     "vip": "1",
     *     "message": "Success"
     * }
     */

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
    @SerializedName("myname")
    private String myname;
    @SerializedName("myscores")
    private int myscores;
    @SerializedName("mycount")
    private int mycount;
    @SerializedName("vip")
    private String vip;
    @SerializedName("message")
    private String message;

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

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @SerializedName("uid")
        private int uid;
        @SerializedName("scores")
        private int scores;
        @SerializedName("name")
        private String name;
        @SerializedName("count")
        private int count;
        @SerializedName("ranking")
        private int ranking;
        @SerializedName("sort")
        private int sort;
        @SerializedName("vip")
        private String vip;
        @SerializedName("imgSrc")
        private String imgSrc;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }
    }
}
