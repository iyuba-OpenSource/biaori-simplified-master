package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.view.textbook.LessonWordContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LessonWordModel implements LessonWordContract.LessonWordModel {

    @Override
    public Disposable getJpWordSentence(int level, int sourceid,
                                        LessonWordContract.JpWordSentenceCallback jpWordSentenceCallback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpWordSentence(level, sourceid)
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
