package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.WordCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.view.textbook.WordDetailsContract;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class WordDetailsModel implements WordDetailsContract.WordDetailsModel {


    @Override
    public Disposable jtest(Map<String, RequestBody> params, WordDetailsContract.EvaluteRecordCallback evaluteRecordCallback) {

        return NetWorkManager
                .getRequestForIUser()
                .jtest(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvaluteRecordBean>() {
                    @Override
                    public void accept(EvaluteRecordBean evaluteRecordBean) throws Exception {

                        evaluteRecordCallback.success(evaluteRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        evaluteRecordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateWordCollect(String userid, String wordid, String type, String appid,
                                        WordDetailsContract.WordCollectCallback wordCollectCallback) {

        return NetWorkManager
                .getRequestForAI()
                .updateWordCollect(userid, wordid, type, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectBean>() {
                    @Override
                    public void accept(WordCollectBean wordCollectBean) throws Exception {

                        wordCollectCallback.success(wordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCollectCallback.error((Exception) throwable);
                    }
                });
    }
}
