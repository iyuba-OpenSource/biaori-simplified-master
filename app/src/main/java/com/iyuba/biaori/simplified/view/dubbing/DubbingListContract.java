package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface DubbingListContract {

    interface DubbingListView extends LoadingView {

        void getTitleBySeriesComplete(SeriesBean seriesBean);
    }

    interface DubbingListPresenter extends IBasePresenter<DubbingListView> {

        void getTitleBySeries(String type, int category, String sign, String format);

        void titleJpPeiYinApi(String type, String format, int pages,
                                    int pageNum, int maxid, int parentID);
    }


    interface DubbingListModel extends BaseModel {


        Disposable getTitleBySeries(String type, int category, String sign, String format, Callback callback);


        Disposable titleJpPeiYinApi(String type, String format, int pages,
                                    int pageNum, int maxid, int parentID, Callback callback);
    }

    interface Callback {

        void success(SeriesBean seriesBean);

        void error(Exception e);
    }
}
