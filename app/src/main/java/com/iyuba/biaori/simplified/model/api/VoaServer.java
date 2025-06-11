package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.entity.DubbingPublish;
import com.iyuba.biaori.simplified.model.bean.dubbing.DelEvalBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingPublishBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.MyDubbingBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PublishEvalBean;
import com.iyuba.biaori.simplified.model.textbook.RankingDetailsBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VoaServer {


    /**
     * 获取配音排行榜
     *
     * @param platform
     * @param format
     * @param protocol
     * @param voaid
     * @param pageNumber
     * @param pageCounts
     * @param sort
     * @param topic
     * @param selectType
     * @return
     */
    @GET("/voa/UnicomApi")
    Observable<DubbingRankBean> getDubbingRank(@Query("platform") String platform, @Query("format") String format,
                                               @Query("protocol") int protocol, @Query("voaid") String voaid,
                                               @Query("pageNumber") int pageNumber, @Query("pageCounts") int pageCounts,
                                               @Query("sort") int sort, @Query("topic") String topic,
                                               @Query("selectType") int selectType);


    /**
     * 发布配音
     *
     * @param protocol       60002
     * @param content        3
     * @param userid
     * @param dubbingPublish 整合参数
     * @return
     */
    @POST("/voa/UnicomApi2")
    Observable<DubbingPublishBean> publishDubbing(@Query("protocol") int protocol, @Query("content") int content
            , @Query("userid") String userid, @Body DubbingPublish dubbingPublish);


    /**
     * 获取
     *
     * @param appid
     * @param uid
     * @param appname
     * @return
     */
    @GET("/voa/getTalkShowOtherWorks.jsp")
    Observable<MyDubbingBean> getTalkShowOtherWorks(@Query("appid") int appid, @Query("uid") String uid, @Query("appname") String appname);


    /**
     * 删除
     *
     * @param protocol
     * @param id
     * @return
     */
    @GET("/voa/UnicomApi")
    Observable<DelEvalBean> delEvalAndDubbing(@Query("protocol") int protocol, @Query("id") int id);


    /**
     * 发布音频
     *
     * @param topic
     * @param content
     * @param format
     * @param idIndex
     * @param paraid
     * @param platform
     * @param protocol
     * @param score
     * @param shuoshuotype
     * @param userid
     * @param name
     * @param voaid
     * @return
     */
    @POST("/voa/UnicomApi")
    @FormUrlEncoded
    Observable<PublishEvalBean> publishEval(@Field("topic") String topic, @Field("content") String content, @Field("format") String format,
                                            @Field("idIndex") int idIndex, @Field("paraid") int paraid, @Field("platform") String platform,
                                            @Field("protocol") int protocol, @Field("score") int score, @Field("shuoshuotype") int shuoshuotype,
                                            @Field("userid") String userid, @Field("name") String name, @Field("voaid") String voaid,
                                            @Field("rewardVersion") String rewardVersion, @Field("appid") String appid);


    /**
     * 获取某个指定用户的已发布音频评测接口(单句+合成)
     *
     * @param uid
     * @param topic
     * @param topicId
     * @param shuoshuoType
     * @param sign
     * @return
     */
    @GET("/voa/getWorksByUserId.jsp")
    Observable<RankingDetailsBean> getWorksByUserId(@Query("uid") int uid, @Query("topic") String topic, @Query("topicId") int topicId,
                                                    @Query("shuoshuoType") String shuoshuoType, @Query("sign") String sign);
}
