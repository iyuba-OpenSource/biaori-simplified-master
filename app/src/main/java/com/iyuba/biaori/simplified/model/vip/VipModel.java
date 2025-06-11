package com.iyuba.biaori.simplified.model.vip;


import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.view.vip.VipContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VipModel implements VipContract.VipModel {


    @Override
    public Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid,
                                  String sign, VipContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getMoreInfo(platform, protocol, id, myid, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean moreInfoBean) throws Exception {

                        callback.success(moreInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getQQGroup(String type, VipContract.JpQQ2Callback callback) {

        return NetWorkManager
                .getRequestForM()
                .getQQGroup(type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean2>() {
                    @Override
                    public void accept(JpQQBean2 jpQQBean2) throws Exception {

                        callback.success(jpQQBean2);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getJpQQ(int appid, VipContract.JpQQCallback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpQQ(appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean>() {
                    @Override
                    public void accept(JpQQBean jpQQBean) throws Exception {

                        callback.success(jpQQBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
