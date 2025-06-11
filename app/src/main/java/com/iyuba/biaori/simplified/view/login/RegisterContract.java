package com.iyuba.biaori.simplified.view.login;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.model.bean.login.VCodeBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RegisterContract {

    interface RegisterView extends LoadingView {


        void getVCodeComplete(boolean b);

        void registerComplete(UserBean.UserinfoDTO userinfoDTO);
    }

    interface RegisterPresenter extends IBasePresenter<RegisterView> {

//        void sendMessage3(String format, String userphone);

        void register(int protocol, String mobile, String username, String password, String platform,
                      int appid, String app, String format, String sign);
    }

    interface RegisterModel extends BaseModel {


        Disposable sendMessage3(String format, String userphone, VCodeCallback callback);

        Disposable register(int protocol, String mobile, String username, String password, String platform,
                            int appid, String app, String format, String sign, UserCallback userCallback);
    }

    interface VCodeCallback {

        void success(VCodeBean vCodeBean);

        void error(Exception e);

    }

    interface UserCallback {

        void success(UserBean.UserinfoDTO userinfoDTO);

        void error(Exception e);
    }
}
