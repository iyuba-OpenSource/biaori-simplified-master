package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PdfFileBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TextbookDetailsModel implements TextbookDetailsContract.TextbookDetailsModel {


    @Override
    public Disposable getJapanesePdfFile(String source, String sourceid, TextbookDetailsContract.PdfCallback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getJapanesePdfFile(source, sourceid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PdfFileBean>() {
                    @Override
                    public void accept(PdfFileBean pdfFileBean) throws Exception {

                        callback.success(pdfFileBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getJapanesePdfFileWithCn(String source, String sourceid, TextbookDetailsContract.PdfCallback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getJapanesePdfFileWithCn(source, sourceid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PdfFileBean>() {
                    @Override
                    public void accept(PdfFileBean pdfFileBean) throws Exception {

                        callback.success(pdfFileBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(String flag, String uid, String appid, String idindex, int srid,
                                  int mobile, TextbookDetailsContract.UpdateScoreCallback updateScoreCallback) {

        return NetWorkManager
                .getRequestForApiCn()
                .updateScore(flag, uid, appid, idindex, srid, mobile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateScoreBean>() {
                    @Override
                    public void accept(UpdateScoreBean updateScoreBean) throws Exception {

                        updateScoreCallback.success(updateScoreBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        updateScoreCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getJpSentence(String name, String sourceid, TextbookDetailsContract.JpSentenceCallback jpSentenceCallback) {

        return NetWorkManager
                .getRequestForIUser()
                .getJpSentence(name, sourceid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpSentenceBean>() {
                    @Override
                    public void accept(JpSentenceBean jpSentenceBean) throws Exception {

                        jpSentenceCallback.success(jpSentenceBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        jpSentenceCallback.error((Exception) throwable);
                    }
                });
    }
}
