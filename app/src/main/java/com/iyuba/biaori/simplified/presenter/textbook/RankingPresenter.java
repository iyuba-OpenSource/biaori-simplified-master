package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.model.bean.textbook.AudioRankingBean;
import com.iyuba.biaori.simplified.model.textbook.RankingModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.RankingContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class RankingPresenter extends BasePresenter<RankingContract.RankingView, RankingContract.RankingModel>
        implements RankingContract.RankingPresenter {


    @Override
    protected RankingContract.RankingModel initModel() {
        return new RankingModel();
    }

    @Override
    public void getTopicRanking(String topic, int topicid, int uid, String type, int start, int total, String sign) {

        Disposable disposable = model.getTopicRanking(topic, topicid, uid, type, start, total, sign, new RankingContract.Callback() {
            @Override
            public void success(AudioRankingBean audioRankingBean) {

                if (audioRankingBean.getResult() != -1) {

                    view.getTopicRanking(audioRankingBean, start);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
