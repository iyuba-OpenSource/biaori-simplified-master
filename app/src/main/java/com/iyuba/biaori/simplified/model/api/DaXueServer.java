package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.entity.ExamRecordPost;
import com.iyuba.biaori.simplified.model.bean.SyncListenBean;
import com.iyuba.biaori.simplified.model.bean.break_through.ExamRecordBean;
import com.iyuba.biaori.simplified.model.bean.me.MyTimeBean;
import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncWordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DaXueServer {


    /**
     * 获取学习时间
     *
     * @param uid
     * @param day
     * @param flg
     * @return
     */
    @GET("/ecollege/getMyTime.jsp")
    Observable<MyTimeBean> getMyTime(@Query("uid") String uid, @Query("day") long day, @Query("flg") int flg);



    @POST("/ecollege/updateExamRecord.jsp")
    Observable<ResponseBody> updateExamRecord(@Body ExamRecordPost bean);


    /**
     * 上传学习记录（语音）
     *
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param Lesson
     * @param TestMode
     * @param TestWords
     * @param platform
     * @param appId
     * @param DeviceId
     * @param LessonId
     * @param sign
     * @return
     */
    @GET("/ecollege/updateStudyRecordNew.jsp")
    Observable<ResponseBody> updateStudyRecordNew(@Query("format") String format, @Query("uid") String uid,
                                                  @Query("BeginTime") String BeginTime, @Query("EndTime") String EndTime,
                                                  @Query("Lesson") String Lesson, @Query("TestMode") String TestMode,
                                                  @Query("TestWords") String TestWords, @Query("platform") String platform,
                                                  @Query("appId") String appId, @Query("DeviceId") String DeviceId,
                                                  @Query("LessonId") String LessonId, @Query("sign") String sign,
                                                  @Query("EndFlg") int EndFlg, @Query("TestNumber") int TestNumber);


    /**
     * 数据同步
     * 获取上传的听力进度接口
     *
     * @param format
     * @param uid
     * @param Pageth
     * @param NumPerPage
     * @param TestMode
     * @param sign
     * @param Lesson     biaori
     * @return
     */
    @GET("/ecollege/getStudyRecordByTestMode.jsp")
    Observable<SyncListenBean> getStudyRecordByTestMode(@Query("format") String format, @Query("uid") int uid, @Query("Pageth") int Pageth,
                                                        @Query("NumPerPage") int NumPerPage, @Query("TestMode") int TestMode, @Query("sign") String sign,
                                                        @Query("Lesson") String Lesson);


    /**
     * 获取音频排行榜
     *
     * @param topic
     * @param topicid
     * @param uid
     * @param type
     * @param start
     * @param total
     * @param sign
     * @return
     */
    @GET("/ecollege/getTopicRanking.jsp")
    Observable<AudioRankingBean> getTopicRanking(@Query("topic") String topic, @Query("topicid") int topicid, @Query("uid") int uid,
                                                 @Query("type") String type, @Query("start") int start, @Query("total") int total,
                                                 @Query("sign") String sign);


    /**
     * 获取口语排行榜
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @param topic
     * @param topicid
     * @param shuoshuotype
     * @return
     */
    @GET("/ecollege/getTopicRanking.jsp")
    Observable<StudyRankingBean> getTopicRanking(@Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                 @Query("sign") String sign, @Query("start") int start, @Query("topic") String topic,
                                                 @Query("topicid") int topicid, @Query("shuoshuotype") int shuoshuotype);



    /**
     * 数据同步
     * 获取单词数据
     *
     * @param appId
     * @param uid
     * @param lesson
     * @param TestMode
     * @param mode
     * @param sign
     * @param format
     * @return
     */
    @GET("/ecollege/getExamDetail.jsp")
    Observable<SyncWordBean> getExamDetail(@Query("appId") int appId, @Query("uid") String uid, @Query("lesson") String lesson,
                                           @Query("TestMode") String TestMode, @Query("mode") int mode, @Query("sign") String sign,
                                           @Query("format") String format);


    /**
     * 获取学习排行榜 同听力排行
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @param mode
     * @return
     */
    @GET("/ecollege/getStudyRanking.jsp")
    Observable<StudyRankingBean> getStudyRanking(@Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                 @Query("sign") String sign, @Query("start") int start, @Query("mode") String mode);


    /**
     * 获取测试排行榜
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @return
     */
    @GET("/ecollege/getTestRanking.jsp")
    Observable<StudyRankingBean> getTestRanking(@Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                @Query("sign") String sign, @Query("start") int start);
}
