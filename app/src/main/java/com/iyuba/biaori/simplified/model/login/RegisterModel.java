package com.iyuba.biaori.simplified.model.login;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.model.bean.login.VCodeBean;
import com.iyuba.biaori.simplified.view.login.RegisterContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterModel implements RegisterContract.RegisterModel {


    @Override
    public Disposable sendMessage3(String format, String userphone, RegisterContract.VCodeCallback callback) {

        return NetWorkManager
                .getRequest()
                .sendMessage3(format, userphone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VCodeBean>() {
                    @Override
                    public void accept(VCodeBean vCodeBean) throws Exception {

                        callback.success(vCodeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable register(int protocol, String mobile, String username, String password, String platform,
                               int appid, String app, String format, String sign, RegisterContract.UserCallback userCallback) {

        return NetWorkManager
                .getRequest()
                .register(protocol, mobile, username, password, platform, appid, app, format, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserBean.UserinfoDTO>() {
                    @Override
                    public void accept(UserBean.UserinfoDTO userinfoDTO) throws Exception {

                        userCallback.success(userinfoDTO);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        userCallback.error((Exception) throwable);
                    }
                });
    }
}
