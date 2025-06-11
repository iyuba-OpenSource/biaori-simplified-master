package com.iyuba.biaori.simplified.model.bean;

import com.google.gson.annotations.SerializedName;
import com.iyuba.biaori.simplified.db.Book;

import java.util.List;

public class BookListBean {


    /**
     * {
     *     "result": 200,
     *     "data": [
     *         {
     *             "id": 1,
     *             "section": "biaori",
     *             "name": "jp3",
     *             "book": "标准日语初级上册",
     *             "source": 1,
     *             "level": "6",
     *             "image": "1.jpg",
     *             "version": 0,
     *             "show": 1
     *         }
     *     ]
     * }
     */
    @SerializedName("result")
    private int result;
    @SerializedName("data")
    private List<Book> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<Book> getData() {
        return data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }

}
