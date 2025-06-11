package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

public class PdfFileBean {

    /**
     * {
     *     "result": "1",
     *     "exists": "true",
     *     "path": "/ceptpdf_jp/1/001.pdf"
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("exists")
    private String exists;
    @SerializedName("path")
    private String path;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
