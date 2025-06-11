package com.iyuba.biaori.simplified.model.me;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.me.WordPdfBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordCollectBean;
import com.iyuba.biaori.simplified.view.me.WordCollectContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WordCollectModel implements WordCollectContract.WordCollectModel {
    @Override
    public Disposable getWordCollect(String userid, String type, int appid, WordCollectContract.Callback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getWordCollect(userid, type, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpWordCollectBean>() {
                    @Override
                    public void accept(JpWordCollectBean jpWordCollectBean) throws Exception {

                        callback.success(jpWordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getWordToPDF(int userid, String type, String ntype, WordCollectContract.WordPdfCallback wordPdfCallback) {

        return NetWorkManager
                .getRequestForAI()
                .getWordToPDF(Constant.GET_WORD_TO_PDF, userid, type, ntype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordPdfBean>() {
                    @Override
                    public void accept(WordPdfBean wordPdfBean) throws Exception {

                        wordPdfCallback.success(wordPdfBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordPdfCallback.error((Exception) throwable);
                    }
                });
    }
}
