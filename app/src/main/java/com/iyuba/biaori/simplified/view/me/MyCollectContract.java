package com.iyuba.biaori.simplified.view.me;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.NewCollectBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MyCollectContract {


    interface MyCollectView extends LoadingView {

        void getCollectComplete(NewCollectBean newCollectBean);
    }


    interface MyCollectPresenter extends IBasePresenter<MyCollectView> {


        void getCollect(int userId, String sign, String topic, int appid, int sentenceFlg);

        void getCollect_wx(String userId, String groupName, String type, int sentenceFlg,
                           int pageNumber, int pageCounts);
    }

    interface MyCollectModel extends BaseModel {


        Disposable getCollect(int userId, String sign, String topic, int appid, int sentenceFlg, CollectCallback collectCallback);

        Disposable getCollect_wx(String userId, String groupName,
                                 String type, int sentenceFlg,
                                 int pageNumber, int pageCounts, NCollectCallback nCollectCallback);
    }

    interface NCollectCallback {

        void success(NewCollectBean newCollectBean);

        void error(Exception e);
    }

    interface CollectCallback {

        void success(CollectBean collectBean);

        void error(Exception e);
    }
}
