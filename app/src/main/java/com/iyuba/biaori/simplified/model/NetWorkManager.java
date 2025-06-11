package com.iyuba.biaori.simplified.model;


import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.api.AiServer;
import com.iyuba.biaori.simplified.model.api.ApiServer;
import com.iyuba.biaori.simplified.model.api.AppsServer;
import com.iyuba.biaori.simplified.model.api.CmsServer;
import com.iyuba.biaori.simplified.model.api.DaXueServer;
import com.iyuba.biaori.simplified.model.api.DevServer;
import com.iyuba.biaori.simplified.model.api.IUserServer;
import com.iyuba.biaori.simplified.model.api.MServer;
import com.iyuba.biaori.simplified.model.api.VipServer;
import com.iyuba.biaori.simplified.model.api.VoaServer;
import com.iyuba.biaori.simplified.util.SSLSocketFactoryUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的框架
 * 网络请求管理者
 */
public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile ApiServer request = null;


    private static Retrofit retrofitForAI;
    private static volatile AiServer requestAI = null;

    private static Retrofit retrofitForIUser;
    private static volatile IUserServer iUserServer;

    private static Retrofit retrofitForApiCn;
    private static volatile ApiServer apiCnServer;

    private static Retrofit retrofitForVip;
    private static volatile VipServer vipRequest;

    private static Retrofit retrofitForApps;
    private static volatile AppsServer appsServer;

    private static Retrofit retrofitForCms;
    private static volatile CmsServer cmsServer;

    //
    private static Retrofit retrofitForM;
    private static volatile MServer mServer;

    private static Retrofit retrofitForDaXue;
    private static volatile DaXueServer daXueServer;

    private static Retrofit retrofitForVoa;
    private static volatile VoaServer voaServer;


    private static Retrofit retrofitDev;
    private static volatile DevServer devServer;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_API_COM_CN)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initAI() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForAI = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_AI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initIUser() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForIUser = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_IUSERSPEECH)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initApiCn() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForApiCn = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initVip() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForVip = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_VIP)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initApps() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForApps = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_APPS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initCms() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForCms = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_CMS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initM() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForM = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_M)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initDaXue() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForDaXue = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_DAXUE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initVoa() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitForVoa = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_VOA)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void initDev() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofitDev = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.URL_DEV)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiServer getRequest() {
        if (request == null) {
            synchronized (ApiServer.class) {
                request = retrofit.create(ApiServer.class);
            }
        }
        return request;
    }


    /**
     * @return
     */
    public static AiServer getRequestForAI() {
        if (requestAI == null) {
            synchronized (AiServer.class) {
                requestAI = retrofitForAI.create(AiServer.class);
            }
        }
        return requestAI;
    }


    public static IUserServer getRequestForIUser() {

        if (iUserServer == null) {
            synchronized (IUserServer.class) {
                iUserServer = retrofitForIUser.create(IUserServer.class);
            }
        }
        return iUserServer;
    }

    public static ApiServer getRequestForApiCn() {

        if (apiCnServer == null) {
            synchronized (ApiServer.class) {
                apiCnServer = retrofitForApiCn.create(ApiServer.class);
            }
        }
        return apiCnServer;
    }

    public static VipServer getRequestForVip() {
        if (vipRequest == null) {
            synchronized (ApiServer.class) {
                vipRequest = retrofitForVip.create(VipServer.class);
            }
        }
        return vipRequest;
    }


    public static AppsServer getRequestForApps() {

        if (appsServer == null) {
            synchronized (AppsServer.class) {
                appsServer = retrofitForApps.create(AppsServer.class);
            }
        }
        return appsServer;
    }

    public static CmsServer getRequestForCms() {

        if (cmsServer == null) {
            synchronized (CmsServer.class) {
                cmsServer = retrofitForCms.create(CmsServer.class);
            }
        }
        return cmsServer;
    }


    public static MServer getRequestForM() {

        if (mServer == null) {
            synchronized (MServer.class) {
                mServer = retrofitForM.create(MServer.class);
            }
        }
        return mServer;
    }

    public static DaXueServer getRequestForDaXue() {

        if (daXueServer == null) {
            synchronized (DaXueServer.class) {
                daXueServer = retrofitForDaXue.create(DaXueServer.class);
            }
        }
        return daXueServer;
    }

    public static VoaServer getRequestForVoa() {
        if (voaServer == null) {
            synchronized (VoaServer.class) {
                voaServer = retrofitForVoa.create(VoaServer.class);
            }
        }
        return voaServer;
    }

    public static DevServer getRequestForDev() {
        if (devServer == null) {
            synchronized (DevServer.class) {
                devServer = retrofitDev.create(DevServer.class);
            }
        }
        return devServer;
    }

    /**
     * vip的接口
     *
     * @return
     */
    /*


    public static void updateManager() {

        getInstance().init();
        getInstance().initAI();
        getInstance().initVoa();
        getInstance().initVip();
        getInstance().initWord();
        getInstance().initIUser();
        getInstance().initDaXue();
//        getInstance().initHost();
    }*/

}
