package com.iyuba.biaori.simplified.model.bean.dubbing;

import com.google.gson.annotations.SerializedName;

public class DubbingPublishBean {

    @SerializedName("AddScore")
    private int addScore;
    @SerializedName("Message")
    private String message;
    @SerializedName("ShuoShuoId")
    private int shuoShuoId;
    @SerializedName("ResultCode")
    private int resultCode;

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getShuoShuoId() {
        return shuoShuoId;
    }

    public void setShuoShuoId(int shuoShuoId) {
        this.shuoShuoId = shuoShuoId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
