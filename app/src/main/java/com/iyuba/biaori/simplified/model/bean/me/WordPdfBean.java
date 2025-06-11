package com.iyuba.biaori.simplified.model.bean.me;

import com.google.gson.annotations.SerializedName;

public class WordPdfBean {


    @SerializedName("result")
    private int result;
    @SerializedName("filePath")
    private String filePath;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
