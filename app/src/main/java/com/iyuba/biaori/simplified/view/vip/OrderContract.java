package com.iyuba.biaori.simplified.view.vip;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.vip.AlipayOrderBean;
import com.iyuba.biaori.simplified.model.bean.vip.PayResultBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface OrderContract {


    interface OrderView extends LoadingView {

        /**
         * 获取到了支付宝订单
         *
         * @param alipayOrderBean
         */
        void getAlipayOrder(AlipayOrderBean alipayOrderBean);

        /**
         * 更新订单成功
         */
        void notifyAliNewComplete();


        /**
         * 成功获取更多数据
         *
         * @param moreInfoBean
         */
        void moreInfoComplete(MoreInfoBean moreInfoBean);

    }

    interface OrderPresenter extends IBasePresenter<OrderView> {

        void alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                         int product_id, String WIDbody, String WIDsubject);

        void notifyAliNew(String data);

        void getMoreInfo(String platform, int protocol, int id, int myid,
                         int appid, String sign);
    }

    interface OrderModel extends BaseModel {

        Disposable alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                               int product_id, String WIDbody, String WIDsubject, Callback callback);

        Disposable notifyAliNew(String data, PayResultCallback payResultCallback);

        Disposable getMoreInfo(String platform, int protocol, int id, int myid,
                               int appid, String sign, MoreInfoCallback moreInfoCallback);
    }

    interface Callback {

        void success(AlipayOrderBean alipayOrderBean);

        void error(Exception e);
    }

    interface PayResultCallback {

        void success(PayResultBean payResultBean);

        void error(Exception e);
    }

    interface MoreInfoCallback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }
}
