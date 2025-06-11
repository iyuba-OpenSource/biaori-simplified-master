package com.iyuba.biaori.simplified.model.bean.me;
import com.iyuba.biaori.simplified.entity.ShareInfoRecord;

import java.util.List;

public class ShareInfoBean {
    public String result;
    public String message;
    public int count;
    public List<ShareInfoRecord> record;


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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ShareInfoRecord> getRecord() {
        return record;
    }

    public void setRecord(List<ShareInfoRecord> record) {
        this.record = record;
    }
}
