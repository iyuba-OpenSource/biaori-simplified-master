package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.model.bean.me.ShareInfoBean;
import com.iyuba.biaori.simplified.model.bean.textbook.NewCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppsServer {


    /**
     * 收藏文章的接口
     *
     * @param groupName
     * @param sentenceFlg 文章传0  句子传1
     * @param appId
     * @param userId
     * @param type        收藏insert   取消收藏del
     * @param voaId
     * @param sentenceId  文章传0   句子传这句话的开始时间 Timing
     * @param topic       biaori
     * @return xml
     */
    @GET("/iyuba/updateCollect.jsp")
    Observable<ResponseBody> updateCollect(@Query("groupName") String groupName, @Query("sentenceFlg") int sentenceFlg, @Query("appId") int appId,
                                           @Query("userId") int userId, @Query("type") String type, @Query("voaId") int voaId,
                                           @Query("sentenceId") int sentenceId, @Query("topic") String topic);


    /**
     * 获取口语秀新闻列表
     *
     * @param type
     * @param format
     * @param pages
     * @param pageNum
     * @param maxid
     * @param parentID
     * @return
     */
    @GET("/iyuba/titleJpPeiYinApi.jsp")
    Observable<SeriesBean> titleJpPeiYinApi(@Query("type") String type, @Query("format") String format, @Query("pages") int pages,
                                            @Query("pageNum") int pageNum, @Query("maxid") int maxid, @Query("parentID") int parentID);


    /**
     * 获取配音的字幕
     *
     * @param format
     * @param voaid
     * @return
     */
    @GET("/iyuba/textExamApi.jsp")
    Observable<DubbingTextBean> textExamApi(@Query("format") String format, @Query("voaid") String voaid);


    /**
     * 添加收藏文章和取消收藏文章
     *
     * @param userId
     * @param voaId
     * @param sentenceId
     * @param groupName
     * @param sentenceFlg
     * @param type
     * @param topic
     * @return
     */
    @GET("/voa/updateCollect_wx.jsp")
    Observable<UCollectBean> updateCollect_wx(@Query("userId") String userId, @Query("voaId") String voaId
            , @Query("sentenceId") int sentenceId, @Query("groupName") String groupName
            , @Query("sentenceFlg") int sentenceFlg, @Query("type") String type
            , @Query("topic") String topic);


    /**
     * 获取收藏的文章
     *
     * @param userId
     * @param groupName
     * @param type
     * @param sentenceFlg
     * @param pageNumber
     * @param pageCounts
     * @return
     */
    @GET("/iyuba/getCollect_wx.jsp")
    Observable<NewCollectBean> getCollect_wx(@Query("userId") String userId, @Query("groupName") String groupName,
                                             @Query("type") String type, @Query("sentenceFlg") int sentenceFlg,
                                             @Query("pageNumber") int pageNumber, @Query("pageCounts") int pageCounts);


    /**
     * 获取打卡历史记录
     *
     * @param uid
     * @param appId
     * @param time
     * @return
     */
    @GET
    Observable<ShareInfoBean> getShareInfoShow(@Url String url, @Query("uid") String uid, @Query("appId") String appId, @Query("time") String time);
}
