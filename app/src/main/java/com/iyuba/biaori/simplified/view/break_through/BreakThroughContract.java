package com.iyuba.biaori.simplified.view.break_through;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface BreakThroughContract {


    interface BreakThroughView extends LoadingView {

        void getJpWordSentenceAllComplete(JpWordSentenceBean jpWordSentenceBean);
    }

    interface BreakThroughPresenter extends IBasePresenter<BreakThroughView> {

        void getJpWordSentenceAll(int level);
    }

    interface BreakThroughModel extends BaseModel {

        Disposable getJpWordSentenceAll(int level, JpWordSentenceCallback jpWordSentenceCallback);
    }

    interface JpWordSentenceCallback {

        void success(JpWordSentenceBean jpWordSentenceBean);

        void error(Exception e);
    }
}
