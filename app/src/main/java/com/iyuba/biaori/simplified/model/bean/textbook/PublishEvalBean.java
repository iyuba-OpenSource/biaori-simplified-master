package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

public class PublishEvalBean {

    /**
     * {
     * "ResultCode": "501",
     * "AddScore": 5,
     * "ShuoshuoId": 19199298,
     * "Message": "OK"
     * }
     */
    @SerializedName("ResultCode")
    private String resultCode;
    @SerializedName("AddScore")
    private int addScore;
    @SerializedName("ShuoshuoId")
    private int shuoshuoId;
    @SerializedName("Message")
    private String message;
    private String reward;


    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public int getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(int shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
