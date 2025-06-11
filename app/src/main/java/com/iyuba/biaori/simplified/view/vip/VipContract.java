package com.iyuba.biaori.simplified.view.vip;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VipContract {


    interface VipView extends LoadingView {

        void getMoreInfoComplete(MoreInfoBean moreInfoBean);

        void showQQDialog(JpQQBean jpQQBean,JpQQBean2 jpQQBean2);
    }

    interface VipPresenter extends IBasePresenter<VipView> {

        void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign);

        void getQQGroup(String type);

        void getJpQQ(int appid, JpQQBean2 jpQQBean2);
    }


    interface VipModel extends BaseModel {


        Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, Callback callback);

        Disposable getQQGroup(String type, JpQQ2Callback callback);

        Disposable getJpQQ(int appid, JpQQCallback callback);
    }

    interface JpQQCallback {

        void success(JpQQBean jpQQBean);

        void error(Exception e);
    }

    interface JpQQ2Callback {

        void success(JpQQBean2 jpQQBean2);

        void error(Exception e);
    }

    interface Callback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }
}
