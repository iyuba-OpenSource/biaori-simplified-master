package com.iyuba.biaori.simplified.view.login;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.login.WxLoginBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface WxLoginContract {

    interface WxLoginView extends LoadingView {


        void getWxAppletToken(WxLoginBean wxLoginBean);

        void getUidByToken(WxLoginBean wxLoginBean);

        void getUserInfo(MoreInfoBean userInfoResponse, String uid);
    }


    interface WxLoginPresenter extends IBasePresenter<WxLoginView> {

        void getWxAppletToken(String platform, String format, String protocol, String appid, String sign);

        void getUidByToken(String platform, String format, String protocol, String token, String sign, String appid);

        void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign);
    }

    interface WxLoginModel extends BaseModel {

        Disposable getWxAppletToken(String platform, String format, String protocol, String appid, String sign, Callback callback);

        Disposable getUidByToken(String platform, String format, String protocol, String token, String sign, String appid, Callback callback);


        Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, UserCallback callback);
    }

    interface UserCallback {

        void success(MoreInfoBean userInfoResponse);

        void error(Exception e);
    }

    interface Callback {

        void success(WxLoginBean wxLoginBean);

        void error(Exception e);
    }
}
