package com.iyuba.biaori.simplified.model.vip;


import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.vip.AlipayOrderBean;
import com.iyuba.biaori.simplified.model.bean.vip.PayResultBean;
import com.iyuba.biaori.simplified.view.vip.OrderContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderModel implements OrderContract.OrderModel {
    @Override
    public Disposable alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                                  int product_id, String WIDbody, String WIDsubject, OrderContract.Callback callback) {
        return NetWorkManager
                .getRequestForVip()
                .alipayOrder(app_id, userId, code, WIDtotal_fee, amount, product_id, WIDbody, WIDsubject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AlipayOrderBean>() {
                    @Override
                    public void accept(AlipayOrderBean alipayOrderBean) throws Exception {

                        callback.success(alipayOrderBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable notifyAliNew(String data, OrderContract.PayResultCallback payResultCallback) {

        return NetWorkManager
                .getRequestForVip()
                .notifyAliNew(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PayResultBean>() {
                    @Override
                    public void accept(PayResultBean payResultBean) throws Exception {

                        payResultCallback.success(payResultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        payResultCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid,
                                  String sign, OrderContract.MoreInfoCallback moreInfoCallback) {

        return NetWorkManager
                .getRequest()
                .getMoreInfo(platform, protocol, id, myid, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean moreInfoBean) throws Exception {

                        moreInfoCallback.success(moreInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        moreInfoCallback.error((Exception) throwable);
                    }
                });
    }
}
