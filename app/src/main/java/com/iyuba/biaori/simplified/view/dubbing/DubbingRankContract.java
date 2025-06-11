package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface DubbingRankContract {


    interface DubbingRankView extends LoadingView {

        void getDubbingRankComplete(DubbingRankBean dubbingRankBean);
    }

    interface DubbingRankPresenter extends IBasePresenter<DubbingRankContract.DubbingRankView> {

        void getDubbingRank(String platform, String format, int protocol, String voaid,
                            int pageNumber, int pageCounts, int sort, String topic,
                            int selectType);
    }

    interface DubbingRankModel extends BaseModel {


        Disposable getDubbingRank(String platform, String format, int protocol, String voaid,
                                  int pageNumber, int pageCounts, int sort, String topic,
                                  int selectType, Callback callback);

    }

    interface Callback {

        void success(DubbingRankBean dubbingRankBean);

        void error(Exception e);
    }
}
