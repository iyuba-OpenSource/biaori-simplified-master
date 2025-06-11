package com.iyuba.biaori.simplified.model.bean.vip;

import com.google.gson.annotations.SerializedName;

public class JpQQBean2 {


    @SerializedName("message")
    private String message;
    @SerializedName("QQ")
    private String qq;
    @SerializedName("key")
    private String key;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
