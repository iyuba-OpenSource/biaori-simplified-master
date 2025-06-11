package com.iyuba.biaori.simplified.view.me;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.me.ShareInfoBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface CalendarContract {

    interface CalendarView extends LoadingView {

        void getShareInfoShowComplete(ShareInfoBean shareInfoBean);
    }

    interface CalendarPresenter extends IBasePresenter<CalendarView> {

        void getShareInfoShow(String uid, String appId, String time);
    }

    interface CalendarModel extends BaseModel {

        Disposable getShareInfoShow(String uid, String appId, String time, Callback callback);
    }


    interface Callback {

        void success(ShareInfoBean shareInfoBean);

        void error(Exception e);
    }
}
