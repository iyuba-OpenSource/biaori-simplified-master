package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface DubbingSecondContract {

    interface DubbingSecondView extends LoadingView {

        void getTitleBySeriesidComplete(SeriesBean seriesBean);
    }

    interface DubbingSecondPresenter extends IBasePresenter<DubbingSecondContract.DubbingSecondView> {

        void getTitleBySeriesid(String type, int seriesid, String sign, String format);
    }

    interface DubbingSecondModel extends BaseModel {

        Disposable getTitleBySeriesid(String type, int seriesid, String sign, String format, Callback callback);
    }

    interface Callback {

        void success(SeriesBean seriesBean);

        void error(Exception e);
    }

}
