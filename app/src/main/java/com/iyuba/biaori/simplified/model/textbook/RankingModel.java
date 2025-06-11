package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.view.textbook.RankingContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankingModel implements RankingContract.RankingModel {


    @Override
    public Disposable getTopicRanking(String topic, int topicid, int uid, String type, int start,
                                      int total, String sign, RankingContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getTopicRanking(topic, topicid, uid, type, start, total, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AudioRankingBean>() {
                    @Override
                    public void accept(AudioRankingBean audioRankingBean) throws Exception {

                        callback.success(audioRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
