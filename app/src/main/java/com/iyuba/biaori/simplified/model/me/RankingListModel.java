package com.iyuba.biaori.simplified.model.me;


import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.me.StudyRankingBean;
import com.iyuba.biaori.simplified.view.me.RankingListContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankingListModel implements RankingListContract.RankingListModel {
    @Override
    public Disposable getStudyRanking(String uid, String type, int total, String sign, int start,
                                      String mode, RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getStudyRanking(uid, type, total, sign, start, mode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTopicRanking(String uid, String type, int total, String sign, int start,
                                      String topic, int topicid, int shuoshuotype, RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getTopicRanking(uid, type, total, sign, start, topic, topicid, shuoshuotype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTestRanking(String uid, String type, int total, String sign, int start,
                                     RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getTestRanking(uid, type, total, sign, start)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
