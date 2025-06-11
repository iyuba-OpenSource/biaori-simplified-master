package com.iyuba.biaori.simplified.model.break_through;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.view.break_through.BreakThroughContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BreakThroughModel implements BreakThroughContract.BreakThroughModel {


    @Override
    public Disposable getJpWordSentenceAll(int level, BreakThroughContract.JpWordSentenceCallback jpWordSentenceCallback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpWordSentenceAll(level)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpWordSentenceBean>() {
                    @Override
                    public void accept(JpWordSentenceBean jpWordSentenceBean) throws Exception {

                        jpWordSentenceCallback.success(jpWordSentenceBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        jpWordSentenceCallback.error((Exception) throwable);
                    }
                });
    }
}
