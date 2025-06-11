package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface RankingContract {

    interface RankingView extends LoadingView {


        void getTopicRanking(AudioRankingBean audioRankingBean, int start);
    }

    interface RankingPresenter extends IBasePresenter<RankingView> {

        void getTopicRanking(String topic, int topicid, int uid, String type,
                             int start, int total, String sign);
    }

    interface RankingModel extends BaseModel {

        Disposable getTopicRanking(String topic, int topicid, int uid, String type,
                                   int start, int total, String sign, Callback callback);
    }

    interface Callback {

        void success(AudioRankingBean audioRankingBean);

        void error(Exception e);
    }
}
