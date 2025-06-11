package com.iyuba.biaori.simplified.model.bean.dubbing;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyDubbingBean {

    /**
     * {
     *     "result": true,
     *     "data": [
     *         {
     *             "ImgSrc": "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=4729911&size=big",
     *             "UserName": "jinrong110",
     *             "Title": "好きなだけ飲ませてやったほうがいいんじゃないか",
     *             "Pic": "http://staticvip.iyuba.cn/images/voa/323288.jpg",
     *             "type": "biaori",
     *             "CreateDate": "2022-03-05 14:51:51",
     *             "score": 4,
     *             "videoUrl": "video/voa/kouyu/2022/3/5/1646463111623.mp4",
     *             "UserId": 4729911,
     *             "agreeCount": 4,
     *             "Title_cn": "就让她喝个够吧",
     *             "TopicId": 323288,
     *             "id": 16219396
     *         }
     *     ],
     *     "count": 5,
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

    public static class DataDTO implements Serializable {
        @SerializedName("ImgSrc")
        private String imgSrc;
        @SerializedName("UserName")
        private String userName;
        @SerializedName("Title")
        private String title;
        @SerializedName("Pic")
        private String pic;
        @SerializedName("type")
        private String type;
        @SerializedName("CreateDate")
        private String createDate;
        @SerializedName("score")
        private int score;
        @SerializedName("videoUrl")
        private String videoUrl;
        @SerializedName("UserId")
        private int userId;
        @SerializedName("agreeCount")
        private int agreeCount;
        @SerializedName("Title_cn")
        private String titleCn;
        @SerializedName("TopicId")
        private int topicId;
        @SerializedName("id")
        private int id;

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getAgreeCount() {
            return agreeCount;
        }

        public void setAgreeCount(int agreeCount) {
            this.agreeCount = agreeCount;
        }

        public String getTitleCn() {
            return titleCn;
        }

        public void setTitleCn(String titleCn) {
            this.titleCn = titleCn;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
