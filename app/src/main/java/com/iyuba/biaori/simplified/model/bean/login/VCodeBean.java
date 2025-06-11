package com.iyuba.biaori.simplified.model.bean.login;

import com.google.gson.annotations.SerializedName;

public class VCodeBean {

    /**
     * {
     * "result":"1",
     * "res_code":"0",
     * "userphone":"18764558277",
     * "identifier":"1657087105031"
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("res_code")
    private String resCode;
    @SerializedName("userphone")
    private String userphone;
    @SerializedName("identifier")
    private String identifier;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
