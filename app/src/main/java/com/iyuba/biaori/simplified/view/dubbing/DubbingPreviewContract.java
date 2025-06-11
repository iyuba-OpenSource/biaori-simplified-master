package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.entity.DubbingPublish;
import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingPublishBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Body;
import retrofit2.http.Header;

public interface DubbingPreviewContract {

    interface DubbingPreviewView extends LoadingView {

        void publishDubbingCompelte(DubbingPublishBean dubbingPublishBean);

        void updateScore(UpdateScoreBean updateScoreBean);
    }

    interface DubbingPreviewPresenter extends IBasePresenter<DubbingPreviewView> {


        void publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish);

        void updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile);
    }

    interface DubbingPreviewModel extends BaseModel {


        Disposable publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish, Callback callback);

        Disposable updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile, TextbookDetailsContract.UpdateScoreCallback updateScoreCallback);
    }

    interface Callback {

        void success(DubbingPublishBean dubbingPublishBean);

        void error(Exception e);
    }
}
