package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CmsServer {


    @GET("/dataapi/jsp/getCollect.jsp")
    Observable<CollectBean> getCollect(@Query("userId") int userId, @Query("sign") String sign, @Query("topic") String topic,
                                       @Query("appid") int appid, @Query("sentenceFlg") int sentenceFlg);


    /**
     * 日语口语秀   获取系列列表
     *
     * @param type
     * @param category
     * @param sign
     * @param format
     * @return
     */
    @GET("/dataapi/jsp/getTitleBySeries.jsp")
    Observable<SeriesBean> getTitleBySeries(@Query("type") String type, @Query("category") int category
            , @Query("sign") String sign, @Query("format") String format);

    /**
     * 获取口语秀二级列表
     *
     * @param type
     * @param seriesid
     * @param sign
     * @param format
     * @return
     */
    @GET("/dataapi/jsp/getTitleBySeries.jsp")
    Observable<SeriesBean> getTitleBySeriesid(@Query("type") String type, @Query("seriesid") int seriesid
            , @Query("sign") String sign, @Query("format") String format);
}
