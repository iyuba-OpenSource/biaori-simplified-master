package com.iyuba.biaori.simplified.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuba.biaori.simplified.db.Book;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;

public class BookUtil {


    public void initBook() {

        String bookStr = "[\n" +
                "        {\n" +
                "            \"id\": 1,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp3\",\n" +
                "            \"book\": \"标准日语初级上册\",\n" +
                "            \"source\": 1,\n" +
                "            \"level\": \"6\",\n" +
                "            \"image\": \"1.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 2,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp3\",\n" +
                "            \"book\": \"标准日语初级下册\",\n" +
                "            \"source\": 2,\n" +
                "            \"level\": \"7\",\n" +
                "            \"image\": \"2.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 3,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp2\",\n" +
                "            \"book\": \"标准日语中级上册\",\n" +
                "            \"source\": 3,\n" +
                "            \"level\": \"8\",\n" +
                "            \"image\": \"3.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 4,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp2\",\n" +
                "            \"book\": \"标准日语中级下册\",\n" +
                "            \"source\": 4,\n" +
                "            \"level\": \"9\",\n" +
                "            \"image\": \"4.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 5,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp1\",\n" +
                "            \"book\": \"标准日语高级上册\",\n" +
                "            \"source\": 5,\n" +
                "            \"level\": \"10\",\n" +
                "            \"image\": \"5.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 6,\n" +
                "            \"section\": \"biaori\",\n" +
                "            \"name\": \"jp1\",\n" +
                "            \"book\": \"标准日语高级下册\",\n" +
                "            \"source\": 6,\n" +
                "            \"level\": \"11\",\n" +
                "            \"image\": \"6.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 7,\n" +
                "            \"section\": \"dajia\",\n" +
                "            \"name\": \"dajia1\",\n" +
                "            \"book\": \"大家的日语初级1\",\n" +
                "            \"source\": 7,\n" +
                "            \"level\": \"12\",\n" +
                "            \"image\": \"7.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 8,\n" +
                "            \"section\": \"dajia\",\n" +
                "            \"name\": \"dajia2\",\n" +
                "            \"book\": \"大家的日语初级2\",\n" +
                "            \"source\": 8,\n" +
                "            \"level\": \"13\",\n" +
                "            \"image\": \"8.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 9,\n" +
                "            \"section\": \"dajia\",\n" +
                "            \"name\": \"dajia3\",\n" +
                "            \"book\": \"大家的日语中级1\",\n" +
                "            \"source\": 15,\n" +
                "            \"level\": \"22\",\n" +
                "            \"image\": \"15.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 10,\n" +
                "            \"section\": \"dajia\",\n" +
                "            \"name\": \"dajia4\",\n" +
                "            \"book\": \"大家的日语中级2\",\n" +
                "            \"source\": 16,\n" +
                "            \"level\": \"23\",\n" +
                "            \"image\": \"16.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 11,\n" +
                "            \"section\": \"gaozhong\",\n" +
                "            \"name\": \"gaozhongriyu1\",\n" +
                "            \"book\": \"高中日语1\",\n" +
                "            \"source\": 9,\n" +
                "            \"level\": \"15\",\n" +
                "            \"image\": \"9.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 12,\n" +
                "            \"section\": \"gaozhong\",\n" +
                "            \"name\": \"gaozhongriyu2\",\n" +
                "            \"book\": \"高中日语2\",\n" +
                "            \"source\": 10,\n" +
                "            \"level\": \"16\",\n" +
                "            \"image\": \"10.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 13,\n" +
                "            \"section\": \"gaozhong\",\n" +
                "            \"name\": \"gaozhongriyu3\",\n" +
                "            \"book\": \"高中日语3\",\n" +
                "            \"source\": 11,\n" +
                "            \"level\": \"17\",\n" +
                "            \"image\": \"11.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 14,\n" +
                "            \"section\": \"chuzhong\",\n" +
                "            \"name\": \"chuzhongriyu1\",\n" +
                "            \"book\": \"初中日语1\",\n" +
                "            \"source\": 12,\n" +
                "            \"level\": \"19\",\n" +
                "            \"image\": \"12.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 15,\n" +
                "            \"section\": \"chuzhong\",\n" +
                "            \"name\": \"chuzhongriyu2\",\n" +
                "            \"book\": \"初中日语2\",\n" +
                "            \"source\": 13,\n" +
                "            \"level\": \"20\",\n" +
                "            \"image\": \"13.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 16,\n" +
                "            \"section\": \"chuzhong\",\n" +
                "            \"name\": \"chuzhongriyu3\",\n" +
                "            \"book\": \"初中日语3\",\n" +
                "            \"source\": 14,\n" +
                "            \"level\": \"21\",\n" +
                "            \"image\": \"14.jpg\",\n" +
                "            \"version\": 0,\n" +
                "            \"show\": 0\n" +
                "        }\n" +
                "    ]";

        int count = LitePal.count(Book.class);
        if (count == 0) {

            Gson gson = new Gson();
            Type type = new TypeToken<List<Book>>() {
            }.getType();
            List<Book> bookList = gson.fromJson(bookStr, type);
            for (int i = 0; i < bookList.size(); i++) {

                bookList.get(i).save();
            }
        }
    }
}
