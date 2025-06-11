package com.iyuba.biaori.simplified.model.bean.dubbing;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DubbingTextBean {

    /**
     * {
     * "total": "11",
     * "Images": "",
     * "voatext": [
     * {
     * "ImgPath": "",
     * "EndTiming": 2.6,
     * "ParaId": "1",
     * "IdIndex": "1",
     * "sentence_cn": "平时就得天天照顾她",
     * "ImgWords": "",
     * "Start_x": "0",
     * "End_y": "0",
     * "Timing": 0.7,
     * "End_x": "0",
     * "Sentence": "普段からおまもりさせられてんのに",
     * "Start_y": "0"
     * }
     * ]
     * }
     */

    @SerializedName("total")
    private String total;
    @SerializedName("Images")
    private String images;
    @SerializedName("voatext")
    private List<VoatextDTO> voatext;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<VoatextDTO> getVoatext() {
        return voatext;
    }

    public void setVoatext(List<VoatextDTO> voatext) {
        this.voatext = voatext;
    }

    public static class VoatextDTO implements Serializable {
        @SerializedName("ImgPath")
        private String imgPath;
        @SerializedName("EndTiming")
        private double endTiming;
        @SerializedName("ParaId")
        private String paraId;
        @SerializedName("IdIndex")
        private String idIndex;
        @SerializedName("sentence_cn")
        private String sentenceCn;
        @SerializedName("ImgWords")
        private String imgWords;
        @SerializedName("Start_x")
        private String startX;
        @SerializedName("End_y")
        private String endY;
        @SerializedName("Timing")
        private double timing;
        @SerializedName("End_x")
        private String endX;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName("Start_y")
        private String startY;

        /**
         * 录制的开始时间
         */
        private long startRecordTime;

        /**
         * 录制的结束时间
         *
         * @return
         */
        private long endRecordTime;

        /**
         * 是否正在录音
         */
        private boolean isRecord = false;

        /**
         * 是否正在播放
         */
        private boolean isPlaying = false;

        /**
         * 录制的位置
         */
        private long recordPosition = 0;

        /**
         * 录音路径
         */
        private String url;

        /**
         * 得分
         */
        private String score = "";


        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public long getRecordPosition() {
            return recordPosition;
        }

        public void setRecordPosition(long recordPosition) {
            this.recordPosition = recordPosition;
        }

        public boolean isRecord() {
            return isRecord;
        }

        public void setRecord(boolean record) {
            isRecord = record;
        }

        public long getStartRecordTime() {
            return startRecordTime;
        }

        public void setStartRecordTime(long startRecordTime) {
            this.startRecordTime = startRecordTime;
        }

        public long getEndRecordTime() {
            return endRecordTime;
        }

        public void setEndRecordTime(long endRecordTime) {
            this.endRecordTime = endRecordTime;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public double getEndTiming() {
            return endTiming;
        }

        public void setEndTiming(double endTiming) {
            this.endTiming = endTiming;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }

        public String getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(String idIndex) {
            this.idIndex = idIndex;
        }

        public String getSentenceCn() {
            return sentenceCn;
        }

        public void setSentenceCn(String sentenceCn) {
            this.sentenceCn = sentenceCn;
        }

        public String getImgWords() {
            return imgWords;
        }

        public void setImgWords(String imgWords) {
            this.imgWords = imgWords;
        }

        public String getStartX() {
            return startX;
        }

        public void setStartX(String startX) {
            this.startX = startX;
        }

        public String getEndY() {
            return endY;
        }

        public void setEndY(String endY) {
            this.endY = endY;
        }

        public double getTiming() {
            return timing;
        }

        public void setTiming(double timing) {
            this.timing = timing;
        }

        public String getEndX() {
            return endX;
        }

        public void setEndX(String endX) {
            this.endX = endX;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getStartY() {
            return startY;
        }

        public void setStartY(String startY) {
            this.startY = startY;
        }
    }
}
