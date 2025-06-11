package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;
import com.iyuba.biaori.simplified.view.textbook.OriginalContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OriginalModel implements OriginalContract.OriginalModel {


    @Override
    public Disposable getJpSentence(String name, String sourceid, OriginalContract.JpSentenceCallback jpSentenceCallback) {

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

    @Override
    public Disposable updateCollect(String groupName, int sentenceFlg, int appId, int userId, String type,
                                    int voaId, int sentenceId, String topic,
                                    OriginalContract.UpdateCollectCallback updateCollectCallback) {

        return NetWorkManager
                .getRequestForApps()
                .updateCollect(groupName, sentenceFlg, appId, userId, type, voaId, sentenceId, topic)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        updateCollectCallback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        updateCollectCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateCollect_wx(String userId, String voaId, int sentenceId, String groupName,
                                       int sentenceFlg, String type, String topic,
                                       OriginalContract.SCollectCallback sCollectCallback) {

        return NetWorkManager
                .getRequestForApps()
                .updateCollect_wx(userId, voaId, sentenceId, groupName, sentenceFlg, type, topic)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UCollectBean>() {
                    @Override
                    public void accept(UCollectBean uCollectBean) throws Exception {

                        sCollectCallback.success(uCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        sCollectCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, OriginalContract.AdCallback callback) {

        return NetWorkManager
                .getRequestForDev()
                .getAdEntryAll(appId, flag, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::success, throwable -> callback.error((Exception) throwable));
    }
}
