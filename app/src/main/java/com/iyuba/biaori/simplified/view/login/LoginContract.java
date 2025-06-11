package com.iyuba.biaori.simplified.view.login;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface LoginContract {

    interface LoginView extends LoadingView {

        void loginComplete(UserBean.UserinfoDTO userBean);
    }


    interface LoginPresenter extends IBasePresenter<LoginView> {


        void login(int protocol, int appid, String username, String password, int x, int y, String sign);
    }

    interface LoginModel extends BaseModel {

        Disposable login(int protocol, int appid, String username, String password, int x,
                         int y, String sign, UserCallback userCallback);
    }

    interface UserCallback {

        void success(UserBean.UserinfoDTO userBean);

        void error(Exception e);
    }
}
