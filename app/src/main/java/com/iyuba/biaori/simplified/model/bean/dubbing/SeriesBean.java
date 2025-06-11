package com.iyuba.biaori.simplified.model.bean.dubbing;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeriesBean {

    /**
     * {
     *     "result": "1",
     *     "total": 5,
     *     "data": [
     *         {
     *             "DescCn": "人気のアニメ　ソング",
     *             "Category": "327",
     *             "SeriesCount": "6",
     *             "SeriesName": "热门动漫歌曲",
     *             "CreateTime": "2020-08-17 07:08:52.0",
     *             "UpdateTime": "2021-06-26 02:06:18.0",
     *             "isVideo": "1",
     *             "HotFlg": "8",
     *             "haveMicro": "0",
     *             "Id": "317",
     *             "pic": "http://static2.iyuba.cn/images/voaseries/317.jpg",
     *             "KeyWords": "歌曲，动漫，经典"
     *         }
     *     ]
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("total")
    private String total;
    @SerializedName("data")
    private List<DataDTO> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("DescCn")
        private String descCn;
        @SerializedName("Category")
        private String category;
        @SerializedName("SeriesCount")
        private String seriesCount;
        @SerializedName("SeriesName")
        private String seriesName;
        @SerializedName("CreateTime")
        private String createTime;
        @SerializedName("UpdateTime")
        private String updateTime;
        @SerializedName("isVideo")
        private String isVideo;
        @SerializedName("HotFlg")
        private String hotFlg;
        @SerializedName("haveMicro")
        private String haveMicro;
        @SerializedName("Id")
        private String id;
        @SerializedName("pic")
        private String pic;
        @SerializedName("KeyWords")
        private String keyWords;
        @SerializedName("IntroDesc")
        private String introDesc;
        @SerializedName("CreatTime")
        private String creatTime;
        @SerializedName("Keyword")
        private String keyword;
        @SerializedName("Title")
        private String title;
        @SerializedName("Sound")
        private String sound;
        @SerializedName("Pic")
        private String picX;
        @SerializedName("VoaId")
        private String voaId;
        @SerializedName("Pagetitle")
        private String pagetitle;
        @SerializedName("Url")
        private String url;
        @SerializedName("Series")
        private int series;
        @SerializedName("Title_cn")
        private String titleCn;
        @SerializedName("PublishTime")
        private String publishTime;
        @SerializedName("ReadCount")
        private String readCount;


        public String getDescCn() {
            return descCn;
        }

        public void setDescCn(String descCn) {
            this.descCn = descCn;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSeriesCount() {
            return seriesCount;
        }

        public void setSeriesCount(String seriesCount) {
            this.seriesCount = seriesCount;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getHotFlg() {
            return hotFlg;
        }

        public void setHotFlg(String hotFlg) {
            this.hotFlg = hotFlg;
        }

        public String getHaveMicro() {
            return haveMicro;
        }

        public void setHaveMicro(String haveMicro) {
            this.haveMicro = haveMicro;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getKeyWords() {
            return keyWords;
        }

        public void setKeyWords(String keyWords) {
            this.keyWords = keyWords;
        }

        public String getIntroDesc() {
            return introDesc;
        }

        public void setIntroDesc(String introDesc) {
            this.introDesc = introDesc;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getPicX() {
            return picX;
        }

        public void setPicX(String picX) {
            this.picX = picX;
        }

        public String getVoaId() {
            return voaId;
        }

        public void setVoaId(String voaId) {
            this.voaId = voaId;
        }

        public String getPagetitle() {
            return pagetitle;
        }

        public void setPagetitle(String pagetitle) {
            this.pagetitle = pagetitle;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSeries() {
            return series;
        }

        public void setSeries(int series) {
            this.series = series;
        }

        public String getTitleCn() {
            return titleCn;
        }

        public void setTitleCn(String titleCn) {
            this.titleCn = titleCn;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }
    }
}
