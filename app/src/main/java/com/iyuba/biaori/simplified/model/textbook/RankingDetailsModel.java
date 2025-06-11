package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.view.textbook.RankingDetailsContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankingDetailsModel implements RankingDetailsContract.RankingDetailsModel {


    @Override
    public Disposable getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign, RankingDetailsContract.Callback callback) {

        return NetWorkManager
                .getRequestForVoa()
                .getWorksByUserId(uid, topic, topicId, shuoshuoType, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RankingDetailsBean>() {
                    @Override
                    public void accept(RankingDetailsBean rankingDetailsBean) throws Exception {

                        callback.success(rankingDetailsBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
