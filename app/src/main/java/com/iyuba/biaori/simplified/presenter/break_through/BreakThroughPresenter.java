package com.iyuba.biaori.simplified.presenter.break_through;

import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.model.break_through.BreakThroughModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.break_through.BreakThroughContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class BreakThroughPresenter extends BasePresenter<BreakThroughContract.BreakThroughView, BreakThroughContract.BreakThroughModel>
        implements BreakThroughContract.BreakThroughPresenter {

    @Override
    protected BreakThroughContract.BreakThroughModel initModel() {
        return new BreakThroughModel();
    }

    @Override
    public void getJpWordSentenceAll(int level) {

        Disposable disposable = model.getJpWordSentenceAll(level, new BreakThroughContract.JpWordSentenceCallback() {
            @Override
            public void success(JpWordSentenceBean jpWordSentenceBean) {

//                view.hideLoading();
                if (jpWordSentenceBean.getResult() == 200) {

                    view.getJpWordSentenceAllComplete(jpWordSentenceBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
