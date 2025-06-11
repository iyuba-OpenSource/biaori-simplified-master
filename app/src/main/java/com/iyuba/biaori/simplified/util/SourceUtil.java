package com.iyuba.biaori.simplified.util;

public class SourceUtil {


    /**
     * 根据Source获取name
     *
     * @param source
     * @return
     */
    public static String getNameFromSource(int source) {

        String name = "jp3";
        if (source == 1 || source == 2) {

            name = "jp3";
        } else if (source == 3 || source == 4) {

            name = "jp2";
        } else if (source == 5 || source == 6) {

            name = "jp1";
        } else if (source == 7) {

            name = "dajia1";
        } else if (source == 8) {

            name = "dajia2";
        } else if (source == 15) {

            name = "biaori";
        } else if (source == 16) {

            name = "dajia4";
        } else if (source == 9) {

            name = "gaozhongriyu1";
        } else if (source == 10) {

            name = "gaozhongriyu2";
        } else if (source == 11) {

            name = "gaozhongriyu3";
        } else if (source == 12) {

            name = "chuzhongriyu1";
        } else if (source == 13) {

            name = "chuzhongriyu2";
        } else if (source == 14) {

            name = "chuzhongriyu3";
        }
        return name;
    }

}
