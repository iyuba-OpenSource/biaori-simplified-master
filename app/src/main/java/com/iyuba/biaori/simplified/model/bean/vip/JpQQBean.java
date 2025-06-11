package com.iyuba.biaori.simplified.model.bean.vip;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JpQQBean {


    @SerializedName("result")
    private int result;
    @SerializedName("data")
    private List<DataDTO> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("editor")
        private int editor;
        @SerializedName("technician")
        private int technician;
        @SerializedName("manager")
        private int manager;

        public int getEditor() {
            return editor;
        }

        public void setEditor(int editor) {
            this.editor = editor;
        }

        public int getTechnician() {
            return technician;
        }

        public void setTechnician(int technician) {
            this.technician = technician;
        }

        public int getManager() {
            return manager;
        }

        public void setManager(int manager) {
            this.manager = manager;
        }
    }
}
