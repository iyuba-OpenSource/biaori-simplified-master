package com.iyuba.biaori.simplified.presenter.vip;


import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.model.vip.VipModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.vip.VipContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class VipPresenter extends BasePresenter<VipContract.VipView, VipContract.VipModel>
        implements VipContract.VipPresenter {

    @Override
    protected VipContract.VipModel initModel() {
        return new VipModel();
    }

    @Override
    public void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign) {

        Disposable disposable = model.getMoreInfo(platform, protocol, id, myid, appid, sign, new VipContract.Callback() {
            @Override
            public void success(MoreInfoBean moreInfoBean) {

                if (moreInfoBean.getResult() == 201) {//成功获取

                    view.getMoreInfoComplete(moreInfoBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时！");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getQQGroup(String type) {

        Disposable disposable = model.getQQGroup(type, new VipContract.JpQQ2Callback() {
            @Override
            public void success(JpQQBean2 jpQQBean2) {

                if (jpQQBean2.getMessage().equals("true")) {

                    //获取客服qq
                    getJpQQ(Constant.APPID, jpQQBean2);
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

    @Override
    public void getJpQQ(int appid, JpQQBean2 jpQQBean2) {

        Disposable disposable = model.getJpQQ(appid, new VipContract.JpQQCallback() {
            @Override
            public void success(JpQQBean jpQQBean) {

                if (jpQQBean.getResult() == 200) {

                    view.showQQDialog(jpQQBean, jpQQBean2);
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
