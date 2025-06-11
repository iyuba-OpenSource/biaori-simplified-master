package com.iyuba.biaori.simplified.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncListenBean {


    @SerializedName("result")
    private String result;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("message")
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @SerializedName("EndTime")
        private String endTime;
        @SerializedName("LessonId")
        private String lessonId;
        @SerializedName("BeginTime")
        private String beginTime;
        @SerializedName("Title")
        private String title;
        @SerializedName("TestNumber")
        private String testNumber;
        @SerializedName("Lesson")
        private String lesson;
        @SerializedName("EndFlg")
        private String endFlg;
        @SerializedName("TestWords")
        private String testWords;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTestNumber() {
            return testNumber;
        }

        public void setTestNumber(String testNumber) {
            this.testNumber = testNumber;
        }

        public String getLesson() {
            return lesson;
        }

        public void setLesson(String lesson) {
            this.lesson = lesson;
        }

        public String getEndFlg() {
            return endFlg;
        }

        public void setEndFlg(String endFlg) {
            this.endFlg = endFlg;
        }

        public String getTestWords() {
            return testWords;
        }

        public void setTestWords(String testWords) {
            this.testWords = testWords;
        }
    }
}
