package com.iyuba.biaori.simplified.view;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface WelcomeContract {

    interface WelcomeView extends LoadingView {

        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }

    interface WelcomePresenter extends IBasePresenter<WelcomeView> {

        void getAdEntryAll(String appId, int flag, String uid);
    }

    interface WelcomeModel extends BaseModel {

        Disposable getAdEntryAll(String appId, int flag, String uid, Callback callback);
    }

    interface Callback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }
}
