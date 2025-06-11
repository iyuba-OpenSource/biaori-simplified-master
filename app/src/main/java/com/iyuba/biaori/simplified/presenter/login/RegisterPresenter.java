package com.iyuba.biaori.simplified.presenter.login;

import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.model.bean.login.VCodeBean;
import com.iyuba.biaori.simplified.model.login.RegisterModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.login.RegisterContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;


public class RegisterPresenter extends BasePresenter<RegisterContract.RegisterView, RegisterContract.RegisterModel>
        implements RegisterContract.RegisterPresenter {


    @Override
    protected RegisterContract.RegisterModel initModel() {
        return new RegisterModel();
    }

//    @Override
//    public void sendMessage3(String format, String userphone) {
//
//        Disposable disposable = model.sendMessage3(format, userphone, new RegisterContract.VCodeCallback() {
//            @Override
//            public void success(VCodeBean vCodeBean) {
//
//                view.getVCodeComplete(vCodeBean);
//
//            }
//
//            @Override
//            public void error(Exception e) {
//
//                view.getVCodeComplete(null);
//                view.toast("获取验证码失败");
//            }
//        });
//        addSubscribe(disposable);
//    }

    @Override
    public void register(int protocol, String mobile, String username, String password, String platform, int appid, String app, String format, String sign) {

        Disposable disposable = model.register(protocol, mobile, username, password, platform,
                appid, app, format, sign, new RegisterContract.UserCallback() {
                    @Override
                    public void success(UserBean.UserinfoDTO userinfoDTO) {

                        if (userinfoDTO.getResult().equals("111")) {

                            view.registerComplete(userinfoDTO);
                        } else if (userinfoDTO.getResult().equals("115")) {

                            view.toast("此号码已注册");
                        } else {

                            view.toast(userinfoDTO.getMessage());
                        }
                    }

                    @Override
                    public void error(Exception e) {

                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }
}
