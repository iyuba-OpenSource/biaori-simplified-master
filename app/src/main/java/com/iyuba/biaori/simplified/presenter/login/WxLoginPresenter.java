package com.iyuba.biaori.simplified.presenter.login;


import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.login.WxLoginBean;
import com.iyuba.biaori.simplified.model.login.WxLoginModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.login.WxLoginContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class WxLoginPresenter extends BasePresenter<WxLoginContract.WxLoginView, WxLoginContract.WxLoginModel>
        implements WxLoginContract.WxLoginPresenter {


    @Override
    protected WxLoginContract.WxLoginModel initModel() {
        return new WxLoginModel();
    }

    @Override
    public void getWxAppletToken(String platform, String format, String protocol, String appid, String sign) {

        Disposable disposable = model.getWxAppletToken(platform, format, protocol, appid, sign, new WxLoginContract.Callback() {

            @Override
            public void success(WxLoginBean wxLoginBean) {


                if (wxLoginBean.getResult() == 200) {

                    view.getWxAppletToken(wxLoginBean);
                } else {
                    view.toast(wxLoginBean.getMessage());
                    view.hideLoading();
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getUidByToken(String platform, String format, String protocol, String token, String sign, String appid) {

        Disposable disposable = model.getUidByToken(platform, format, protocol, token, sign, appid,
                new WxLoginContract.Callback() {
                    @Override
                    public void success(WxLoginBean wxLoginBean) {

                        if (wxLoginBean.getResult() == 200) {

                            view.getUidByToken(wxLoginBean);
                        } else if (wxLoginBean.getResult() == 302) {

                            view.toast("微信小程序登录失败");
                            view.hideLoading();
                        } else {

                            view.toast(wxLoginBean.getMessage());
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void error(Exception e) {

                        view.hideLoading();
                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign) {

        Disposable disposable = model.getMoreInfo(platform, protocol, id, myid, appid, sign, new WxLoginContract.UserCallback() {
            @Override
            public void success(MoreInfoBean userInfoResponse) {

                view.getUserInfo(userInfoResponse, myid + "");
                view.hideLoading();
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
