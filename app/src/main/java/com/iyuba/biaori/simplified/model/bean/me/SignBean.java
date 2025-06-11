package com.iyuba.biaori.simplified.model.bean.me;

public class SignBean {

    private String result;
    private String addcredit;//增加的积分数
    private String totalcredit;//当前总积分数
    private String days;//连续打卡天数
    private String money; // 此时获得的money数量,单位是分
    private String message;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAddcredit() {
        return addcredit;
    }

    public void setAddcredit(String addcredit) {
        this.addcredit = addcredit;
    }

    public String getTotalcredit() {
        return totalcredit;
    }

    public void setTotalcredit(String totalcredit) {
        this.totalcredit = totalcredit;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
