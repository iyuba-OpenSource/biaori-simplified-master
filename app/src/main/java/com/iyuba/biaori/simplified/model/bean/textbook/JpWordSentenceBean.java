package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;
import com.iyuba.biaori.simplified.db.JpWord;

import java.util.List;

public class JpWordSentenceBean {

    /**
     * {
     * "result": 200,
     * "size": 39,
     * "data": [
     * {
     * "id": 9191,
     * "sound": "001_01",
     * "word": "中国人",
     * "word_ch": "中国人",
     * "pron": "ちゅうごくじん",
     * "sentence": "李さんは中国人です。",
     * "sentence_ch": "小李是中国人。",
     * "speech": "名",
     * "wordNum": 0,
     * "source": "br",
     * "sourceid": "1",
     * "idindex": "1",
     * "level": "6"
     * }
     * ]
     * }
     */

    @SerializedName("result")
    private int result;
    @SerializedName("size")
    private int size;
    @SerializedName("data")
    private List<JpWord> data;

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

    public List<JpWord> getData() {
        return data;
    }

    public void setData(List<JpWord> data) {
        this.data = data;
    }

}
