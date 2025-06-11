package com.iyuba.biaori.simplified.model.api;


import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.login.LogoffBean;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.model.bean.login.VCodeBean;
import com.iyuba.biaori.simplified.model.bean.login.WxLoginBean;
import com.iyuba.biaori.simplified.model.bean.me.RewardBean;
import com.iyuba.biaori.simplified.model.bean.textbook.ReadSubmitBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {


    /**
     * 根据手机号码和密码登录
     *
     * @param protocol 10010
     * @param appid    241
     * @param username 手机号码或者用户名
     * @param password 密码
     * @param x
     * @param y
     * @return
     */
    @GET("/v2/api.iyuba")
    Observable<UserBean.UserinfoDTO> login(@Query("protocol") int protocol, @Query("appid") int appid, @Query("username") String username,
                                           @Query("password") String password, @Query("x") int x,
                                           @Query("y") int y, @Query("sign") String sign);


    /**
     * 获取验证码
     *
     * @param format
     * @param userphone
     * @return
     */
    @GET("/sendMessage3.jsp")
    Observable<VCodeBean> sendMessage3(@Query("format") String format, @Query("userphone") String userphone);


    /**
     * 注册
     *
     * @param protocol
     * @param mobile
     * @param username
     * @param password
     * @param platform
     * @param appid
     * @param app
     * @param format
     * @param sign
     * @return
     */
    @GET("/v2/api.iyuba")
    Observable<UserBean.UserinfoDTO> register(@Query("protocol") int protocol, @Query("mobile") String mobile, @Query("username") String username,
                                              @Query("password") String password, @Query("platform") String platform, @Query("appid") int appid,
                                              @Query("app") String app, @Query("format") String format, @Query("sign") String sign);

    /**
     * 扣除积分
     * <p>
     * 日语的规则是  100 拼接上 lesson 拼接上 lessonid ，保证唯一即可
     *
     * @param srid
     * @param mobile
     * @param flag
     * @param uid
     * @param appid
     * @param idindex
     * @return
     */
    @GET("/credits/updateScore.jsp")
    Observable<UpdateScoreBean> updateScore(@Query("flag") String flag
            , @Query("uid") String uid, @Query("appid") String appid
            , @Query("idindex") String idindex, @Query("srid") int srid
            , @Query("mobile") int mobile);


    /**
     * 获取更多的用户数据
     *
     * @param platform
     * @param protocol
     * @param id
     * @param myid
     * @param appid
     * @param sign
     * @return
     */
    @GET("/v2/api.iyuba")
    Observable<MoreInfoBean> getMoreInfo(@Query("platform") String platform, @Query("protocol") int protocol,
                                         @Query("id") int id, @Query("myid") int myid,
                                         @Query("appid") int appid, @Query("sign") String sign);


    /**
     * 注销
     *
     * @param protocol
     * @param username
     * @param password
     * @param format
     * @param sign
     * @return
     */
    @GET("/v2/api.iyuba")
    Observable<LogoffBean> logoff(@Query("protocol") int protocol, @Query("username") String username,
                                  @Query("password") String password, @Query("format") String format,
                                  @Query("sign") String sign);


    /**
     * 获取微信小程序的token
     *
     * @param platform
     * @param format
     * @param protocol
     * @param appid
     * @param sign
     * @return
     */
    @GET
    Observable<WxLoginBean> getWxAppletToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                             @Query("protocol") String protocol, @Query("appid") String appid,
                                             @Query("sign") String sign);


    /**
     * 通过获取token获取uid
     *
     * @param platform
     * @param format
     * @param protocol
     * @param token
     * @return
     */
    @GET
    Observable<WxLoginBean> getUidByToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                          @Query("protocol") String protocol, @Query("token") String token, @Query("sign") String sign,
                                          @Query("appid") String appid);

    /**
     * 我的钱包
     *
     * @param uid
     * @param pages
     * @param pageCount
     * @param sign
     * @return
     */
    @GET
    Observable<RewardBean> getUserActionRecord(@Url String url, @Query("uid") int uid, @Query("pages") int pages,
                                               @Query("pageCount") int pageCount, @Query("sign") String sign);



    /**
     * 提交阅读记录
     *
     * @param url
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param appName
     * @param Lesson
     * @param LessonId
     * @param appId
     * @param Device
     * @param DeviceId
     * @param EndFlg
     * @param wordcount
     * @param categoryid
     * @param platform
     * @return
     */
    @GET
    Observable<ReadSubmitBean> updateNewsStudyRecord(@Url String url, @Query("format") String format, @Query("uid") int uid, @Query("BeginTime") String BeginTime,
                                                     @Query("EndTime") String EndTime, @Query("appName") String appName, @Query("Lesson") String Lesson, @Query("LessonId") int LessonId,
                                                     @Query("appId") int appId, @Query("Device") String Device, @Query("DeviceId") String DeviceId, @Query("EndFlg") int EndFlg,
                                                     @Query("wordcount") int wordcount, @Query("categoryid") int categoryid, @Query("platform") String platform,
                                                     @Query("rewardVersion") int rewardVersion);
}
