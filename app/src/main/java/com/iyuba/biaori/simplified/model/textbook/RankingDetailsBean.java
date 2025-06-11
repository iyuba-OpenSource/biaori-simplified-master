package com.iyuba.biaori.simplified.model.textbook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankingDetailsBean {

    /**
     * {
     *     "result": true,
     *     "data": [
     *         {
     *             "paraid": 0,
     *             "score": 17,
     *             "shuoshuotype": 4,
     *             "againstCount": 0,
     *             "agreeCount": 0,
     *             "TopicId": 1,
     *             "ShuoShuo": "wav6/202302/jp3/20230213/16762803596336002.mp3",
     *             "id": 19220148,
     *             "idIndex": 0,
     *             "CreateDate": "2023-02-13 17:26:26"
     *         }
     *     ],
     *     "count": 1,
     *     "message": "查询成功"
     * }
     */

    @SerializedName("result")
    private boolean result;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("count")
    private int count;
    @SerializedName("message")
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @SerializedName("paraid")
        private int paraid;
        @SerializedName("score")
        private int score;
        @SerializedName("shuoshuotype")
        private int shuoshuotype;
        @SerializedName("againstCount")
        private int againstCount;
        @SerializedName("agreeCount")
        private int agreeCount;
        @SerializedName("TopicId")
        private int topicId;
        @SerializedName("ShuoShuo")
        private String shuoShuo;
        @SerializedName("id")
        private int id;
        @SerializedName("idIndex")
        private int idIndex;
        @SerializedName("CreateDate")
        private String createDate;

        public int getParaid() {
            return paraid;
        }

        public void setParaid(int paraid) {
            this.paraid = paraid;
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

        public int getAgainstCount() {
            return againstCount;
        }

        public void setAgainstCount(int againstCount) {
            this.againstCount = againstCount;
        }

        public int getAgreeCount() {
            return agreeCount;
        }

        public void setAgreeCount(int agreeCount) {
            this.agreeCount = agreeCount;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public String getShuoShuo() {
            return shuoShuo;
        }

        public void setShuoShuo(String shuoShuo) {
            this.shuoShuo = shuoShuo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(int idIndex) {
            this.idIndex = idIndex;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
