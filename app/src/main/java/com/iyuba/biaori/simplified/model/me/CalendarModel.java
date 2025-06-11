package com.iyuba.biaori.simplified.model.me;


import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.me.ShareInfoBean;
import com.iyuba.biaori.simplified.view.me.CalendarContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CalendarModel implements CalendarContract.CalendarModel {


    @Override
    public Disposable getShareInfoShow(String uid, String appId, String time, CalendarContract.Callback callback) {

        return NetWorkManager
                .getRequestForApps()
                .getShareInfoShow(Constant.GET_SHARE_INFO_SHOW, uid, appId, time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShareInfoBean>() {
                    @Override
                    public void accept(ShareInfoBean shareInfoBean) throws Exception {

                        callback.success(shareInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
