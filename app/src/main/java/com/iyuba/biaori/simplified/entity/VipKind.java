package com.iyuba.biaori.simplified.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * vip类型
 */
public class VipKind {

    private int icon;

    private String name;

    private String name2;

    /**
     * 会员类型
     */
    private int vipFlag;

    /**
     * 会员说明
     */
    private int vip_intro;


    private List<Kind> kindList;


    public VipKind(int icon, String name, String name2, int vipFlag) {
        this.icon = icon;
        this.name = name;
        this.name2 = name2;
        this.vipFlag = vipFlag;
        this.kindList = new ArrayList<>();
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(int vipFlag) {
        this.vipFlag = vipFlag;
    }

    public int getVip_intro() {
        return vip_intro;
    }

    public void setVip_intro(int vip_intro) {
        this.vip_intro = vip_intro;
    }


    public List<Kind> getKindList() {
        return kindList;
    }

    public void setKindList(List<Kind> kindList) {
        this.kindList = kindList;
    }

    /**
     * 存储价格与文字
     */
    public static class Kind {

        private String text;

        private int price;

        private int month;

        public Kind(String text, int price) {
            this.text = text;
            this.price = price;
        }

        public Kind(String text, int price, int month) {
            this.text = text;
            this.price = price;
            this.month = month;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
