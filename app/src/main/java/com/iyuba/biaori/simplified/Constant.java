package com.iyuba.biaori.simplified;


import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;

public class Constant {


    /**
     * 阅读速度
     */
    public final static int NORMAL_WPM = 600;

    public final static String TOPIC = "biaori";


    public static int WORD_QUANTITY = 30;

    /**
     * 存储用户的信息
     */
    public static UserBean.UserinfoDTO userinfo;

    /**
     * 选择的书籍
     */
    public static Book bookDataDTO;

    public final static int APPID = 271;

    public final static int ADAPPID = 2711;


    /**
     * 微课的OWERID
     */
    public final static int OWERID = 6;

    /**
     * 短域名
     */
    public static String DOMAIN = "iyuba.cn";

    /**
     * 长域名
     */
    public static String DOMAINLong = "iyuba.com.cn";

    public static String URL_API = "http://api." + DOMAIN;

    public static String URL_API_COM_CN = "http://api." + DOMAINLong;

    public static String URL_AI = "http://ai." + DOMAIN;

    public static String URL_STATIC2 = "http://static2." + DOMAIN;

    public static String URL_IUSERSPEECH = "http://iuserspeech." + DOMAIN + ":9001";

    public static String URL_STATIC1 = "http://static1." + DOMAIN;

    public static String URL_VIP = "http://vip." + DOMAIN;

    public static String URL_APPS = "http://apps." + DOMAIN;

    public static String URL_APP = "http://app." + DOMAIN;

    public static String URL_CMS = "http://cms." + DOMAIN;

    public static String URL_M = "http://m." + DOMAIN;

    public static String URL_DAXUE = "http://daxue." + DOMAIN;

    public static String URL_VOA = "http://voa." + DOMAIN;

    public static String URL_LESSON_SHARE = URL_AI + "/jpbook/sentence/getSentence?";

    public static String URL_VIDEO = "http://static0." + DOMAIN;

    public static String URL_STATICVIP = "http://staticvip." + DOMAIN;

    public static String URL_DEV = "http://dev." + DOMAIN;

    public static String URL_STATIC3 = "http://static3." + DOMAIN;


    public static String JAPAN_AUDIO_BASE_URL_20210623 = URL_STATIC2 + "/Japan/";


    /**
     * 获取收藏单词的pdf
     */
    public static String GET_WORD_TO_PDF = URL_AI + "/management/getJpWordToPDF.jsp";


    /**
     * 上传阅读记录（学习）
     */
    public static String UPDATE_NEWS_STUDY_RECORD = URL_DAXUE + "/ecollege/updateNewsStudyRecord.jsp";


    /**
     * 我的钱包记录
     */
    public static String GET_USER_ACTION_RECORD = URL_API + "/credits/getuseractionrecord.jsp";


    // 课本的封面图片
    public static String BOOK_COVER_IMAGE = URL_STATIC2 + "/Japan/image/";


    /**
     * 微信登录
     */
    public static String URL_WX_LOGIN = URL_API_COM_CN + "/v2/api.iyuba";


    /**
     * 获取打卡历史记录
     */
    public static String GET_SHARE_INFO_SHOW = URL_APP + "/getShareInfoShow.jsp";

    /**
     * 选择书的sp
     */
    public final static String SP_BOOK = "BOOK";

    public final static String SP_KEY_BOOK_INFO = "BOOK_INFO";

    /**
     * 存储登录的用户信息
     */
    public final static String SP_USER = "USER";

    public final static String SP_KEY_USER_INFO = "USER_INFO";


    /**
     * 隐私协议的确认状态
     */
    public final static String SP_PRIVACY = "PRIVACY";

    public final static String SP_KEY_PRIVACY_STATE = "PRIVACY_STATE";


    /**
     * 隐私协议的确认状态
     */
    public final static String SP_PERMISSION = "PERMISSION";

    public final static String SP_KEY_PERMISSION_RECORD = "RECORD_AUDIO";


    /**
     * 存储单词闯关的单词数
     */
    public final static String SP_BREAK_THROUGH = "BREAK_THROUGH";

    public final static String SP_KEY_WORD_NUM = "WORD_NUM";


    /**
     * 下载
     */
    public final static String SP_DOWNLOAD = "DOWNLOAD";

    /**
     * 非会员的下载pdf的次数
     */
    public final static String SP_DOWNLOAD_COUNT = "DOWNLOAD_COUNT";


    /**
     * 用户协议
     */
    public static String URL_PROTOCOLUSE;


    /**
     * 隐私政策
     */
    public static String URL_PROTOCOLPRI;


    /**
     * 初始化广播
     */
    public final static String ACTION_INIT = "com.iyuba.biaori.simplified.init";


    /**
     * 是否开启购买会员0.01
     */
    public final static boolean IS_PAY_DEBUG = false;


    /**
     * 权限
     */
    public final static String SP_PERMISSIONS = "PERMISSIONS";


    /**
     * 录音
     * 1 拒绝
     * 0 未申请过此权限
     */
    public final static String SP_KEY_RECORD = "RECORD";


    //广告
    public static final String AD_ADS1 = "ads1";//倍孜
    public static final String AD_ADS2 = "ads2";//创见
    public static final String AD_ADS3 = "ads3";//头条穿山甲
    public static final String AD_ADS4 = "ads4";//广点通优量汇
    public static final String AD_ADS5 = "ads5";//快手

}
