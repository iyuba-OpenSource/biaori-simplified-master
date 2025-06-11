package com.iyuba.biaori.simplified.presenter.me;


import com.iyuba.biaori.simplified.model.bean.me.ShareInfoBean;
import com.iyuba.biaori.simplified.model.me.CalendarModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.CalendarContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class CalendarPresenter extends BasePresenter<CalendarContract.CalendarView, CalendarContract.CalendarModel>
        implements CalendarContract.CalendarPresenter {

    @Override
    protected CalendarContract.CalendarModel initModel() {
        return new CalendarModel();
    }

    @Override
    public void getShareInfoShow(String uid, String appId, String time) {

        Disposable disposable = model.getShareInfoShow(uid, appId, time, new CalendarContract.Callback() {
            @Override
            public void success(ShareInfoBean shareInfoBean) {

                if ("200".equals(shareInfoBean.getResult())) {

                    view.getShareInfoShowComplete(shareInfoBean);
                }else{
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
}
