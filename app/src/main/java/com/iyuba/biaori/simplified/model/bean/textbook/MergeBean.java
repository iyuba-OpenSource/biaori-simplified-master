package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

public class MergeBean {

    /**
     * {
     *     "result": "1",
     *     "message": "merge success",
     *     "URL": "wav6/202302/jp3/20230213/16762678907725986.mp3"
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;
    @SerializedName("URL")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
