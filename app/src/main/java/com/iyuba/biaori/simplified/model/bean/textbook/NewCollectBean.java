package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewCollectBean {


    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private String result;
    @SerializedName("pageNumber")
    private int pageNumber;
    @SerializedName("firstPage")
    private int firstPage;
    @SerializedName("lastPage")
    private int lastPage;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("counts")
    private int counts;
    @SerializedName("totalPage")
    private int totalPage;
    @SerializedName("nextPage")
    private int nextPage;
    @SerializedName("prevPage")
    private int prevPage;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public static class DataDTO {
        @SerializedName("voaid")
        private String voaid;
        @SerializedName("sound")
        private String sound;
        @SerializedName("contentcn")
        private String contentcn;
        @SerializedName("topic")
        private String topic;
        @SerializedName("pic")
        private String pic;
        @SerializedName("content")
        private String content;
        @SerializedName("createDate")
        private String createDate;

        private String source;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getVoaid() {
            return voaid;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getContentcn() {
            return contentcn;
        }

        public void setContentcn(String contentcn) {
            this.contentcn = contentcn;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
