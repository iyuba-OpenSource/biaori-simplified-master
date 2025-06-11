package com.iyuba.biaori.simplified.model.bean.dubbing;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DubbingRankBean {


    @SerializedName("ResultCode")
    private String resultCode;
    @SerializedName("Message")
    private String message;
    @SerializedName("PageNumber")
    private int pageNumber;
    @SerializedName("TotalPage")
    private int totalPage;
    @SerializedName("FirstPage")
    private int firstPage;
    @SerializedName("PrevPage")
    private int prevPage;
    @SerializedName("NextPage")
    private int nextPage;
    @SerializedName("LastPage")
    private int lastPage;
    @SerializedName("AddScore")
    private int addScore;
    @SerializedName("Counts")
    private int counts;
    @SerializedName("data")
    private List<DataDTO> data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("ImgSrc")
        private String imgSrc;
        @SerializedName("image")
        private String image;
        @SerializedName("backId")
        private int backId;
        @SerializedName("backList")
        private String backList;
        @SerializedName("UserName")
        private String userName;
        @SerializedName("ShuoShuoType")
        private String shuoShuoType;
        @SerializedName("ShuoShuo")
        private String shuoShuo;
        @SerializedName("TopicCategory")
        private String topicCategory;
        @SerializedName("title")
        private String title;
        @SerializedName("CreateDate")
        private String createDate;
        @SerializedName("score")
        private String score;
        @SerializedName("paraid")
        private String paraid;
        @SerializedName("topicid")
        private String topicid;
        @SerializedName("againstCount")
        private String againstCount;
        @SerializedName("videoUrl")
        private String videoUrl;
        @SerializedName("Userid")
        private String userid;
        @SerializedName("agreeCount")
        private String agreeCount;
        @SerializedName("id")
        private String id;
        @SerializedName("idIndex")
        private String idIndex;
        @SerializedName("vip")
        private String vip;

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getBackId() {
            return backId;
        }

        public void setBackId(int backId) {
            this.backId = backId;
        }

        public String getBackList() {
            return backList;
        }

        public void setBackList(String backList) {
            this.backList = backList;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getShuoShuoType() {
            return shuoShuoType;
        }

        public void setShuoShuoType(String shuoShuoType) {
            this.shuoShuoType = shuoShuoType;
        }

        public String getShuoShuo() {
            return shuoShuo;
        }

        public void setShuoShuo(String shuoShuo) {
            this.shuoShuo = shuoShuo;
        }

        public String getTopicCategory() {
            return topicCategory;
        }

        public void setTopicCategory(String topicCategory) {
            this.topicCategory = topicCategory;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getParaid() {
            return paraid;
        }

        public void setParaid(String paraid) {
            this.paraid = paraid;
        }

        public String getTopicid() {
            return topicid;
        }

        public void setTopicid(String topicid) {
            this.topicid = topicid;
        }

        public String getAgainstCount() {
            return againstCount;
        }

        public void setAgainstCount(String againstCount) {
            this.againstCount = againstCount;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAgreeCount() {
            return agreeCount;
        }

        public void setAgreeCount(String agreeCount) {
            this.agreeCount = agreeCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(String idIndex) {
            this.idIndex = idIndex;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }
    }
}
