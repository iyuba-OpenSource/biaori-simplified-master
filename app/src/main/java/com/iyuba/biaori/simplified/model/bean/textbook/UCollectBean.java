package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

public class UCollectBean {

    /**
     * {
     *     "msg": "success",
     *     "result": "200"
     * }
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private String result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
