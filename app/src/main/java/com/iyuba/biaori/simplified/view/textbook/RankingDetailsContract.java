package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.textbook.RankingDetailsBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface RankingDetailsContract {


    interface RankingDetailsView extends LoadingView {


        void getMergeData(RankingDetailsBean rankingDetailsBean);
    }

    interface RankingDetailsPresenter extends IBasePresenter<RankingDetailsView> {

        void getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign);
    }

    interface RankingDetailsModel extends BaseModel {


        Disposable getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign, Callback callback);
    }

    interface Callback {

        void success(RankingDetailsBean rankingDetailsBean);

        void error(Exception e);
    }
}
