package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;
import com.iyuba.biaori.simplified.db.JpWord;

import java.util.List;

public class JpWordCollectBean {

    /**
     * {
     * "result": "1",
     * "message": "collectwords.",
     * "data": [
     * {
     * "id": "9194",
     * "word": "アメリカ人",
     * "word_ch": "美国人",
     * "pron": "アメリカじん",
     * "speech": "名",
     * "sentence": "スミスさんはアメリカ人です。",
     * "sentence_ch": "史密斯是美国人。",
     * "source": "br",
     * "sourceid": "1",
     * "idindex": "4",
     * "level": "6",
     * "wordSound": "http://static2.iyuba.cn/Japan/jp3/word/001/001_04.mp3",
     * "sentenceSound": "http://static2.iyuba.cn/Japan/jp3/wordExamples/001/001_04.mp3",
     * "sound": "http://static2.iyuba.cn/Japan/jp3/word/001/001_04.mp3"
     * }
     * ]
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<JpWord> data;

    public List<JpWord> getData() {
        return data;
    }

    public void setData(List<JpWord> data) {
        this.data = data;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
