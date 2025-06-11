package com.iyuba.biaori.simplified.presenter.vip;


import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.vip.AlipayOrderBean;
import com.iyuba.biaori.simplified.model.bean.vip.PayResultBean;
import com.iyuba.biaori.simplified.model.vip.OrderModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.vip.OrderContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class OrderPresenter extends BasePresenter<OrderContract.OrderView, OrderContract.OrderModel>
        implements OrderContract.OrderPresenter {


    @Override
    protected OrderContract.OrderModel initModel() {
        return new OrderModel();
    }

    @Override
    public void alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount, int product_id, String WIDbody, String WIDsubject) {

        Disposable disposable = model.alipayOrder(app_id, userId, code, WIDtotal_fee, amount,
                product_id, WIDbody, WIDsubject, new OrderContract.Callback() {
                    @Override
                    public void success(AlipayOrderBean alipayOrderBean) {

                        if (alipayOrderBean.getResult().equals("200")) {

                            view.getAlipayOrder(alipayOrderBean);
                        } else {

                            view.toast(alipayOrderBean.getMessage());
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void error(Exception e) {


                        view.hideLoading();
                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时！");
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void notifyAliNew(String data) {

        Disposable disposable = model.notifyAliNew(data, new OrderContract.PayResultCallback() {
            @Override
            public void success(PayResultBean payResultBean) {

                if (payResultBean.getCode().equals("200")) {

                    view.notifyAliNewComplete();
                } else {

                    view.toast(payResultBean.getMsg());
                    view.hideLoading();
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时！");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign) {

        Disposable disposable = model.getMoreInfo(platform, protocol, id, myid, appid, sign, new OrderContract.MoreInfoCallback() {
            @Override
            public void success(MoreInfoBean moreInfoBean) {

                if (moreInfoBean.getResult() == 201) {//成功获取

                    view.moreInfoComplete(moreInfoBean);
                } else {
                    view.toast(moreInfoBean.getMessage());
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
