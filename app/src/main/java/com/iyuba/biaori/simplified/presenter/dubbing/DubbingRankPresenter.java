package com.iyuba.biaori.simplified.presenter.dubbing;

import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;
import com.iyuba.biaori.simplified.model.dubbing.DubbingRankModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.DubbingRankContract;

import io.reactivex.disposables.Disposable;

public class DubbingRankPresenter extends BasePresenter<DubbingRankContract.DubbingRankView, DubbingRankContract.DubbingRankModel>
        implements DubbingRankContract.DubbingRankPresenter {


    @Override
    protected DubbingRankContract.DubbingRankModel initModel() {
        return new DubbingRankModel();
    }

    @Override
    public void getDubbingRank(String platform, String format, int protocol, String voaid, int pageNumber, int pageCounts, int sort, String topic, int selectType) {

        Disposable disposable = model.getDubbingRank(platform, format, protocol, voaid, pageNumber,
                pageCounts, sort, topic, selectType, new DubbingRankContract.Callback() {
                    @Override
                    public void success(DubbingRankBean dubbingRankBean) {

                        if (dubbingRankBean.getResultCode().equals("511")) {

                            view.getDubbingRankComplete(dubbingRankBean);
                        }
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
        addSubscribe(disposable);
    }
}
