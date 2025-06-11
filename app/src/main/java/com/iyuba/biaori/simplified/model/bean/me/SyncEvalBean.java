package com.iyuba.biaori.simplified.model.bean.me;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 评测数据同步bean
 */
public class SyncEvalBean {

    /**
     *
     * newsid== lessonid
     * newstype确定是哪两本书
     * {
     *     "result": "1",
     *     "size": 4,
     *     "data": [
     *         {
     *             "sentence": "はい，そうです。小野です。",
     *             "paraid": 1,
     *             "score": "0.75",
     *             "newsid": 1,
     *             "idindex": 2,
     *             "userid": 13801378,
     *             "url": "wav6/202302/jp3/20230213/16762744438028896.mp3",
     *             "newstype": "jp3"
     *         }
     *     ]
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("size")
    private int size;
    @SerializedName("data")
    private List<DataDTO> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("sentence")
        private String sentence;
        @SerializedName("paraid")
        private int paraid;
        @SerializedName("score")
        private String score;
        @SerializedName("newsid")
        private int newsid;
        @SerializedName("idindex")
        private int idindex;
        @SerializedName("userid")
        private int userid;
        @SerializedName("url")
        private String url;
        @SerializedName("newstype")
        private String newstype;

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public int getParaid() {
            return paraid;
        }

        public void setParaid(int paraid) {
            this.paraid = paraid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getNewsid() {
            return newsid;
        }

        public void setNewsid(int newsid) {
            this.newsid = newsid;
        }

        public int getIdindex() {
            return idindex;
        }

        public void setIdindex(int idindex) {
            this.idindex = idindex;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNewstype() {
            return newstype;
        }

        public void setNewstype(String newstype) {
            this.newstype = newstype;
        }
    }
}
