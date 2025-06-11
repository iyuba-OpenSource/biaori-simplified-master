package com.iyuba.biaori.simplified.model.api;


import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.model.bean.JpLessonBean;
import com.iyuba.biaori.simplified.model.bean.WordCollectBean;
import com.iyuba.biaori.simplified.model.bean.me.GroupBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncEvalBean;
import com.iyuba.biaori.simplified.model.bean.me.WordPdfBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PdfFileBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AiServer {


    /**
     * @param uid
     * @param appid
     * @param platform Build.BRAND
     * @return
     */
    @GET("/japanapi/getJpBook.jsp")
    Observable<BookListBean> getJpBook(@Query("uid") int uid, @Query("appid") int appid, @Query("platform") String platform);


    /**
     * 获取某一课本的课程
     *
     * @param source 课本的source
     * @return
     */
    @GET("/japanapi/getJpLesson.jsp")
    Observable<JpLessonBean> getJpLesson(@Query("source") String source);


    /**
     * 获取日文pdf文件
     *
     * @param source
     * @param sourceid
     * @return
     */
    @GET("/management/getJapanesePdfFile.jsp")
    Observable<PdfFileBean> getJapanesePdfFile(@Query("source") String source, @Query("sourceid") String sourceid);

    /**
     * 获取中文英文pdf文件
     *
     * @param source
     * @param sourceid
     * @return
     */
    @GET("/management/getJapanesePdfFileWithCn.jsp")
    Observable<PdfFileBean> getJapanesePdfFileWithCn(@Query("source") String source, @Query("sourceid") String sourceid);


    /**
     * 获取QQ
     *
     * @param appid 自定义的appid
     * @return
     */
    @GET("/japanapi/getJpQQ.jsp")
    Observable<JpQQBean> getJpQQ(@Query("appid") int appid);


    /**
     * 根据课文source和课文id来获取单词
     *
     * @param level    课本source
     * @param sourceid 课文id
     * @return
     */
    @GET("/japanapi/getJpWordSentence.jsp")
    Observable<JpWordSentenceBean> getJpWordSentence(@Query("level") int level, @Query("sourceid") int sourceid);


    /**
     * 根据level获取日语单词
     *
     * @param level
     * @return
     */
    @GET("/japanapi/getJpWordSentenceAll.jsp")
    Observable<JpWordSentenceBean> getJpWordSentenceAll(@Query("level") int level);


    /**
     * 收藏单词和取消单词
     *
     * @param userid
     * @param wordid
     * @param type
     * @param appid
     * @return
     */
    @GET("/api/updateWordCollect.jsp")
    Observable<WordCollectBean> updateWordCollect(@Query("userid") String userid, @Query("wordid") String wordid,
                                                  @Query("type") String type, @Query("appid") String appid);


    /**
     * 获取收藏的单词
     *
     * @param userid
     * @param type
     * @param appid
     * @return
     */
    @GET("/api/getWordCollect.jsp")
    Observable<JpWordCollectBean> getWordCollect(@Query("userid") String userid, @Query("type") String type, @Query("appid") int appid);


    /**
     * 获取评测句子的同步数据
     *
     * @param userId
     * @param newstype
     * @return
     */
    @GET("/management/getTestRecord.jsp")
    Observable<SyncEvalBean> getTestRecord(@Query("userId") String userId, @Query("newstype") String newstype);



    /**
     * 获取官方群
     *
     * @param uid
     * @param apptype
     * @return
     */
    @GET("/nlp/entergroup")
    Observable<GroupBean> entergroup(@Query("uid") String uid, @Query("apptype") String apptype);


    /**
     * 获取单词pdf   链接地址
     * <p>
     * 标日/初高中/大家的：  type=query ntype =null
     * N1/N2/N3：         type=null ntype =N1(N2或N3)
     *
     * @param userid
     * @param type
     * @param ntype
     * @return
     */
    @GET
    Observable<WordPdfBean> getWordToPDF(@Url String url, @Query("userid") int userid, @Query("type") String type, @Query("ntype") String ntype);
}
