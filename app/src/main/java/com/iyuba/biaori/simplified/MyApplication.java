package com.iyuba.biaori.simplified;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.util.LoginUtil;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.data.local.HeadlineInfoHelper;
import com.iyuba.headlinelibrary.data.local.db.HLDBManager;
import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.iyuba.share.ShareExecutor;
import com.iyuba.share.mob.MobShareExecutor;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.ui.publish.PublishDoingActivity;
import personal.iyuba.personalhomelibrary.ui.widget.dialog.ShareBottomDialog;
import personal.iyuba.personalhomelibrary.utils.UserUtil;
import timber.log.Timber;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        Timber.plant(new Timber.DebugTree());

        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.preInit(this, "5cd135373fc195127d0000aa", "");
        PrivacyInfoHelper.init(this);
        IMoocDBManager.init(this);

        NetWorkManager.getInstance().init();
        NetWorkManager.getInstance().initAI();
        NetWorkManager.getInstance().initIUser();
        NetWorkManager.getInstance().initApiCn();
        NetWorkManager.getInstance().initVip();
        NetWorkManager.getInstance().initApps();
        NetWorkManager.getInstance().initCms();
        NetWorkManager.getInstance().initM();
        NetWorkManager.getInstance().initDaXue();
        NetWorkManager.getInstance().initVoa();
        NetWorkManager.getInstance().initDev();
        //数据库初始化
        LitePal.initialize(this);

        //初始化默认课本
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SP_BOOK, MODE_PRIVATE);
        String bookInfo = sharedPreferences.getString(Constant.SP_KEY_BOOK_INFO, null);
        if (bookInfo == null) {

            String jsonStr = "{" +
                    "            \"id\": 1," +
                    "            \"section\": \"biaori\"," +
                    "            \"name\": \"jp3\"," +
                    "            \"book\": \"标准日语初级上册\"," +
                    "            \"source\": 1," +
                    "            \"level\": \"6\"," +
                    "            \"image\": \"1.jpg\"," +
                    "            \"version\": 0," +
                    "            \"show\": 1" +
                    "        }";
            Constant.bookDataDTO = new Gson().fromJson(jsonStr, Book.class);
        } else {

            Constant.bookDataDTO = new Gson().fromJson(bookInfo, Book.class);
        }
        //隐私政策

        //华为
        //山东爱语言 "http://iuserspeech.iyuba.cn:9001/api/protocoluse666.jsp?company=4&apptype=" + getString(R.string.app_name);
        //山东爱语言 "http://iuserspeech.iyuba.cn:9001/api/protocolpri.jsp?company=4&apptype=" + getString(R.string.app_name);


        /**
         * 万云天
         * 使用协议：https://www.ibbc.net.cn/protocoluse.jsp?company=1&apptype=%E6%97%A5%E8%AF%AD%E5%90%AC%E5%8A%9B
         * 隐私政策：https://www.ibbc.net.cn/protocolpri.jsp?company=1&apptype=%E6%97%A5%E8%AF%AD%E5%90%AC%E5%8A%9B
         */

        /**
         * 济南珠穆拉玛
         *   Constant.URL_PROTOCOLUSE = "https://www.ibbc.net.cn/protocoluse.jsp?company=5&apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8");
         *
         *   Constant.URL_PROTOCOLPRI = "https://www.ibbc.net.cn/protocolpri.jsp?company=5&apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8");
         */

        /**
         * 北京爱语吧
         * Constant.URL_AI + "/api/protocoluse666.jsp?apptype=" +URLEncoder.encode(getString(R.string.app_name), "UTF-8") + "&company=" + 1;
         * Constant.URL_AI + "/api/protocolpri.jsp?apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8") + "&company=" + 1;
         */
        /**
         * 山东爱语吧信息
         *  Constant.URL_PROTOCOLUSE = "https://www.ibbc.net.cn/protocoluse.jsp?company=4&apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8");
         *  Constant.URL_PROTOCOLPRI = "https://www.ibbc.net.cn/protocolpri.jsp?company=4&apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8");
         */

        try {
            Constant.URL_PROTOCOLUSE = Constant.URL_AI + "/api/protocoluse666.jsp?apptype=" +URLEncoder.encode(getString(R.string.app_name), "UTF-8") + "&company=" + 1;

            Constant.URL_PROTOCOLPRI = Constant.URL_AI + "/api/protocolpri.jsp?apptype=" + URLEncoder.encode(getString(R.string.app_name), "UTF-8") + "&company=" + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //视频模块
        IHeadline.resetMseUrl();
        IHeadline.setExtraMseUrl(Constant.URL_IUSERSPEECH + "/jtest/");
        IHeadline.setExtraMergeAudioUrl(Constant.URL_IUSERSPEECH + "/jtest/merge/");

        //用户是否登录
        SharedPreferences spUser = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        String userStr = spUser.getString(Constant.SP_KEY_USER_INFO, null);
        if (userStr != null) {

            Constant.userinfo = new Gson().fromJson(userStr, UserBean.UserinfoDTO.class);
            //共通模块
            User user = new User();
            user.name = Constant.userinfo.getUsername();
            if (Constant.userinfo.isVip()) {

                user.vipStatus = Constant.userinfo.getVipStatus();
            } else {

                user.vipStatus = 0 + "";
            }
            user.uid = Constant.userinfo.getUid();
            IyuUserManager.getInstance().setCurrentUser(user);
            init();
        }
        SharedPreferences sp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);
        int pState = sp.getInt(Constant.SP_KEY_PRIVACY_STATE, 0);
        if (pState == 1) {

            init();
        } else {

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(Constant.ACTION_INIT));
        }
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            init();
        }
    };


    private synchronized void init() {

        MobSDK.submitPolicyGrantResult(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");

        YdConfig.getInstance().init(this, Constant.APPID + "");

        YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);
        YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
        YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
        YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
        YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
        YouDaoAd.getYouDaoOptions().setWifiEnabled(false);
        YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
        YoudaoSDK.init(this);

        DLManager.init(this, 8);

        //分享
        List<String> list = new ArrayList<>();
        list.add("QQ");
        list.add("QQ空间");
        list.add("微信好友");
        list.add("微信收藏");
        list.add("微信朋友圈");

        ShareBottomDialog.setSharedPlatform(list);
        MobShareExecutor executor = new MobShareExecutor();
        ShareExecutor.getInstance().setRealExecutor(executor);

        //个人中心
        IUniversityPicker.init(this);
        PersonalHome.init(this, Constant.APPID + "", Constant.TOPIC);
        PersonalHome.setMainPath(MainActivity.class.getName());
        HLDBManager.init(this);
        PublishDoingActivity.IS_JAPANESE_APP = true;//日语
        //我的收藏

        //设置我的收藏 过滤
        BasicFavor.init(this, Constant.APPID + "");
        List<String> typeFilter = new ArrayList<>();
        typeFilter.add(HeadlineType.SMALLVIDEO_JP);
        typeFilter.add(HeadlineType.JAPANVIDEOS);
        BasicFavor.setTypeFilter(typeFilter);

        BasicFavorInfoHelper.init(this);
        BasicFavorDBManager.init(this);
        //headline
        HeadlineInfoHelper.init(this);
        IHeadline.init(this, Constant.APPID + "", Constant.TOPIC);
        IHeadline.setEnableShare(true);
        IHeadline.setEnableGoStore(false);
//        Pair pair = new Pair<>(2130903070, 2130903069);
//        IHeadline.setCategoryArrayId(HeadlineType.SMALLVIDEO_JP, pair);

        DLManager.init(context, 8);//下载初始化
        BasicDLDBManager.init(context);
        //微课
        IMooc.init(this, Constant.APPID + "", Constant.TOPIC);
        IMooc.setEnableShare(true);
        PrivacyInfoHelper.init(this);
        //微课的隐私协议
        PrivacyInfoHelper.getInstance().putApproved(true);

    }

    public static Context getContext() {
        return context;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        context = null;
    }
}
