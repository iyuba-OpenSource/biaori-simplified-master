package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface IUserServer {

    /**
     * 获取sourceid
     *
     * @param name
     * @param sourceid
     * @return
     */
    @GET("/japanapi/getJpSentence.jsp")
    Observable<JpSentenceBean> getJpSentence(@Query("name") String name, @Query("sourceid") String sourceid);


    /**
     * 评测单词
     *
     * @param params
     * @return
     */
    @POST("/jtest/")
    @Multipart
    Observable<EvaluteRecordBean> jtest(@PartMap Map<String, RequestBody> params);


    /**
     * 评测
     *
     * @param body
     * @return
     */
    @POST("/jtest")
    Observable<EvaluteRecordBean> jtest(@Body RequestBody body);


    /**
     * 合成
     * /jtest/merge/  最后的/不能省略，省略后不
     *
     * @param audios
     * @param type
     * @return
     */
    @POST("/jtest/merge/")
    @FormUrlEncoded
    Observable<MergeBean> merge(@Field("audios") String audios, @Field("type") String type);

}
