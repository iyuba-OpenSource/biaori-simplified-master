package com.iyuba.biaori.simplified.view.me;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RankingListContract {

    interface RankingListView extends LoadingView {

        /**
         * 学习和听力使用
         *
         * @param studyRankingBean
         * @param mode
         */
        void getStudyRanking(StudyRankingBean studyRankingBean, String mode);

        void getTopicRanking(StudyRankingBean studyRankingBean);


        void getTestRanking(StudyRankingBean studyRankingBean);


        /**
         * @param studyRankingBean
         * @param mode             RankingListAdapter.KINDS
         */
        void loadmore(StudyRankingBean studyRankingBean, int flag);
    }


    interface RankingListPresenter extends IBasePresenter<RankingListView> {

        /**
         * 及时处理掉不需要的请求
         */
        void clearDisposable();


        void getStudyRanking(String uid, String type, int total,
                             String sign, int start, String mode);

        void getTopicRanking(String uid, String type, int total,
                             String sign, int start, String topic,
                             int topicid, int shuoshuotype);


        void getTestRanking(String uid, String type, int total,
                            String sign, int start);
    }

    interface RankingListModel extends BaseModel {


        Disposable getStudyRanking(String uid, String type, int total,
                                   String sign, int start, String mode, Callback callback);

        Disposable getTopicRanking(String uid, String type, int total,
                                   String sign, int start, String topic,
                                   int topicid, int shuoshuotype, Callback callback);


        Disposable getTestRanking(String uid, String type, int total,
                                  String sign, int start, Callback callback);
    }


    interface Callback {

        void success(StudyRankingBean studyRankingBean);

        void error(Exception e);
    }
}
