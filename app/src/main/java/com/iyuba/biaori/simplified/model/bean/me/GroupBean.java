package com.iyuba.biaori.simplified.model.bean.me;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupBean {


    @SerializedName("result")
    private String result;
    @SerializedName("gpinfo")
    private List<GpinfoDTO> gpinfo;
    @SerializedName("hostnum")
    private int hostnum;
    @SerializedName("membernum")
    private int membernum;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<GpinfoDTO> getGpinfo() {
        return gpinfo;
    }

    public void setGpinfo(List<GpinfoDTO> gpinfo) {
        this.gpinfo = gpinfo;
    }

    public int getHostnum() {
        return hostnum;
    }

    public void setHostnum(int hostnum) {
        this.hostnum = hostnum;
    }

    public int getMembernum() {
        return membernum;
    }

    public void setMembernum(int membernum) {
        this.membernum = membernum;
    }

    public static class GpinfoDTO {
        @SerializedName("uid")
        private String uid;
        @SerializedName("userstats")
        private String userstats;
        @SerializedName("groupid")
        private String groupid;
        @SerializedName("gptitle")
        private String gptitle;
        @SerializedName("gpdesc")
        private String gpdesc;
        @SerializedName("gpimg")
        private String gpimg;
        @SerializedName("gptype")
        private String gptype;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUserstats() {
            return userstats;
        }

        public void setUserstats(String userstats) {
            this.userstats = userstats;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGptitle() {
            return gptitle;
        }

        public void setGptitle(String gptitle) {
            this.gptitle = gptitle;
        }

        public String getGpdesc() {
            return gpdesc;
        }

        public void setGpdesc(String gpdesc) {
            this.gpdesc = gpdesc;
        }

        public String getGpimg() {
            return gpimg;
        }

        public void setGpimg(String gpimg) {
            this.gpimg = gpimg;
        }

        public String getGptype() {
            return gptype;
        }

        public void setGptype(String gptype) {
            this.gptype = gptype;
        }
    }
}
