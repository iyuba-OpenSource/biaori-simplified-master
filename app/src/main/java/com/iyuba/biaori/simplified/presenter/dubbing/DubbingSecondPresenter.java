package com.iyuba.biaori.simplified.presenter.dubbing;

import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.model.dubbing.DubbingSecondModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.DubbingSecondContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class DubbingSecondPresenter extends BasePresenter<DubbingSecondContract.DubbingSecondView, DubbingSecondContract.DubbingSecondModel>
        implements DubbingSecondContract.DubbingSecondPresenter {


    @Override
    protected DubbingSecondContract.DubbingSecondModel initModel() {
        return new DubbingSecondModel();
    }

    @Override
    public void getTitleBySeriesid(String type, int seriesid, String sign, String format) {

        Disposable disposable = model.getTitleBySeriesid(type, seriesid, sign, format, new DubbingSecondContract.Callback() {
            @Override
            public void success(SeriesBean seriesBean) {

                if (seriesBean.getResult().equals("1")) {

                    view.getTitleBySeriesidComplete(seriesBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求失败");
                }
            }
        });
        addSubscribe(disposable);
    }
}
