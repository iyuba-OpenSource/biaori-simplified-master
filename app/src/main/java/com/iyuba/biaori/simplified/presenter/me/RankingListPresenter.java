package com.iyuba.biaori.simplified.presenter.me;

import android.util.Log;


import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;
import com.iyuba.biaori.simplified.model.me.RankingListModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.RankingListContract;

import io.reactivex.disposables.Disposable;

public class RankingListPresenter extends BasePresenter<RankingListContract.RankingListView, RankingListContract.RankingListModel>
        implements RankingListContract.RankingListPresenter {


    @Override
    protected RankingListContract.RankingListModel initModel() {
        return new RankingListModel();
    }

    @Override
    public void clearDisposable() {

        if (compositeDisposable != null) {

            compositeDisposable.clear();
        }
    }

    @Override
    public void getStudyRanking(String uid, String type, int total, String sign, int start, String mode) {

        Disposable disposable = model.getStudyRanking(uid, type, total, sign, start, mode, new RankingListContract.Callback() {
            @Override
            public void success(StudyRankingBean studyRankingBean) {

                if (start == 0) {//初次加载

                    if (studyRankingBean.getResult() != 0) {

                        view.getStudyRanking(studyRankingBean, mode);
                    } else {
                        view.toast("没有数据");
                    }
                } else {

                    if (studyRankingBean.getResult() == 10) {//还有数据

                        view.loadmore(studyRankingBean, 1);
                    } else {

                        view.loadmore(studyRankingBean, 0);
                    }
                }
            }

            @Override
            public void error(Exception e) {

                view.loadmore(null, 1);
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTopicRanking(String uid, String type, int total, String sign, int start, String topic, int topicid, int shuoshuotype) {

        Disposable disposable = model.getTopicRanking(uid, type, total, sign, start, topic, topicid, shuoshuotype, new RankingListContract.Callback() {
            @Override
            public void success(StudyRankingBean studyRankingBean) {

                if (start == 0) {
                    if (studyRankingBean.getResult() != 0) {

                        view.getTopicRanking(studyRankingBean);
                    } else {
                        view.loadmore(null, 0);
                        view.toast("没有数据");
                    }
                } else {

                    if (studyRankingBean.getResult() == 10) {//还有数据

                        view.loadmore(studyRankingBean, 1);
                    } else {

                        view.loadmore(studyRankingBean, 0);
                    }
                }

            }

            @Override
            public void error(Exception e) {

                Log.d("qwe", "");
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTestRanking(String uid, String type, int total, String sign, int start) {

        Disposable disposable = model.getTestRanking(uid, type, total, sign, start, new RankingListContract.Callback() {
            @Override
            public void success(StudyRankingBean studyRankingBean) {

                if (start == 0) {

                    if (studyRankingBean.getResult() != 0) {

                        view.getTestRanking(studyRankingBean);
                    } else {
                        view.loadmore(null, 0);
                        view.toast("没有数据");
                    }
                } else {

                    if (studyRankingBean.getResult() == 10) {//还有数据

                        view.loadmore(studyRankingBean, 1);
                    } else {

                        view.loadmore(studyRankingBean, 0);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }
}


