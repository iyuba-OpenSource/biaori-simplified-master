package com.iyuba.biaori.simplified.model.bean;

import com.google.gson.annotations.SerializedName;
import com.iyuba.biaori.simplified.db.JpLesson;

import java.util.List;

public class JpLessonBean {

    /**
     * {
     *     "result": 200,
     *     "size": 24,
     *     "data": [
     *         {
     *             "lesson": "第1課　李さんは中国人です。",
     *             "lessonCH": "小李是中国人。",
     *             "lessonID": "001",
     *             "source": "1",
     *             "progressListen": null,
     *             "version": "0",
     *             "sentenceNum": "18",
     *             "wordNum": "39"
     *         }
     *     ]
     * }
     */

    @SerializedName("result")
    private int result;
    @SerializedName("size")
    private int size;
    @SerializedName("data")
    private List<JpLesson> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<JpLesson> getData() {
        return data;
    }

    public void setData(List<JpLesson> data) {
        this.data = data;
    }

}
