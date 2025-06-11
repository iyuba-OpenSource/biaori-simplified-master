package com.iyuba.biaori.simplified.model.bean.vip;

import com.google.gson.annotations.SerializedName;

public class AlipayOrderBean {


    @SerializedName("alipayTradeStr")
    private String alipayTradeStr;
    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;

    public String getAlipayTradeStr() {
        return alipayTradeStr;
    }

    public void setAlipayTradeStr(String alipayTradeStr) {
        this.alipayTradeStr = alipayTradeStr;
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
