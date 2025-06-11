package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.model.textbook.RankingDetailsBean;
import com.iyuba.biaori.simplified.model.textbook.RankingDetailsModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.RankingDetailsContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class RankingDetailsPresenter extends BasePresenter<RankingDetailsContract.RankingDetailsView, RankingDetailsContract.RankingDetailsModel>
        implements RankingDetailsContract.RankingDetailsPresenter {


    @Override
    protected RankingDetailsContract.RankingDetailsModel initModel() {
        return new RankingDetailsModel();
    }

    @Override
    public void getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign) {

        Disposable disposable = model.getWorksByUserId(uid, topic, topicId, shuoshuoType, sign, new RankingDetailsContract.Callback() {

            @Override
            public void success(RankingDetailsBean rankingDetailsBean) {

                if (rankingDetailsBean.isResult()) {

                    view.getMergeData(rankingDetailsBean);
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
