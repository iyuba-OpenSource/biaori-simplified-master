package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;
import com.iyuba.biaori.simplified.db.Sentence;

import java.util.List;

public class JpSentenceBean {

    /**
     * {
     * "result": 200,
     * "size": 18,
     * "data": [
     * {
     * "announcer": "甲",
     * "sentence": "わたしは李です。小野さんですか。",
     * "sentencech": "我姓李，您是小野女士吗？",
     * "source": "jp3",
     * "sourceid": "001",
     * "idindex": "01",
     * "sound": "001_01",
     * "startTime": 37,
     * "endTime": 41
     * }
     * ]
     * }
     */

    @SerializedName("result")
    private int result;
    @SerializedName("size")
    private int size;
    @SerializedName("data")
    private List<Sentence> data;

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

    public List<Sentence> getData() {
        return data;
    }

    public void setData(List<Sentence> data) {
        this.data = data;
    }

}
