package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.DelEvalBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.MyDubbingBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MyDubbingContract {

    interface MyDubbingView extends LoadingView {

        void getMyDubbingComplete(MyDubbingBean myDubbingBean);

        void delEvalComplete(int id);

    }

    interface MyDubbingPresenter extends IBasePresenter<MyDubbingView> {

        void getTalkShowOtherWorks(int appid, String uid, String appname);

        void delEvalAndDubbing(int protocol, int id);
    }

    interface MyDubbingModel extends BaseModel {

        Disposable getTalkShowOtherWorks(int appid, String uid, String appname, Callback callback);

        Disposable delEvalAndDubbing(int protocol, int id, DelCallback delCallback);
    }

    interface Callback {

        void success(MyDubbingBean myDubbingBeans);

        void error(Exception e);
    }

    interface DelCallback {

        void success(DelEvalBean delEvalBean);

        void error(Exception e);
    }
}
