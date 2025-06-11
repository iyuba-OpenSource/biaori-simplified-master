package com.iyuba.biaori.simplified.presenter.dubbing;

import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.model.dubbing.DubbingListModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.DubbingListContract;

import io.reactivex.disposables.Disposable;

public class DubbingListPresenter extends BasePresenter<DubbingListContract.DubbingListView, DubbingListContract.DubbingListModel>
        implements DubbingListContract.DubbingListPresenter {

    @Override
    protected DubbingListContract.DubbingListModel initModel() {
        return new DubbingListModel();
    }

    @Override
    public void getTitleBySeries(String type, int category, String sign, String format) {

        view.showLoading(null);
        Disposable disposable = model.getTitleBySeries(type, category, sign, format, new DubbingListContract.Callback() {
            @Override
            public void success(SeriesBean seriesBean) {

                view.hideLoading();
                if (seriesBean.getResult().equals("1")) {

                    view.getTitleBySeriesComplete(seriesBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void titleJpPeiYinApi(String type, String format, int pages, int pageNum, int maxid, int parentID) {

        view.showLoading(null);
        Disposable disposable = model.titleJpPeiYinApi(type, format, pages, pageNum, maxid, parentID, new DubbingListContract.Callback() {
            @Override
            public void success(SeriesBean seriesBean) {

                view.hideLoading();
                if (seriesBean.getResult().equals("200")) {

                    view.getTitleBySeriesComplete(seriesBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
            }
        });
        addSubscribe(disposable);
    }
}
