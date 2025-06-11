package com.iyuba.biaori.simplified.model;

import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.JpLessonBean;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.view.TextbookContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TextbookModel implements TextbookContract.TextbookModel {


    @Override
    public Disposable getJpLesson(String source, TextbookContract.JpLessonCallback jpLessonCallback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpLesson(source)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpLessonBean>() {
                    @Override
                    public void accept(JpLessonBean jpLessonBean) throws Exception {

                        jpLessonCallback.success(jpLessonBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        jpLessonCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getCollect(int userId, String sign, String topic, int appid, int sentenceFlg,
                                 TextbookContract.CollectCallback collectCallback) {

        return NetWorkManager
                .getRequestForCms()
                .getCollect(userId, sign, topic, appid, sentenceFlg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CollectBean>() {
                    @Override
                    public void accept(CollectBean collectBean) throws Exception {

                        collectCallback.success(collectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        collectCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, TextbookContract.ADCallback callback) {

        return NetWorkManager
                .getRequestForDev()
                .getAdEntryAll(appId, flag, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::success, throwable -> callback.error((Exception) throwable));
    }
}
