package com.iyuba.biaori.simplified.model.me;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.NewCollectBean;
import com.iyuba.biaori.simplified.view.me.MyCollectContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyCollectModel implements MyCollectContract.MyCollectModel {


    @Override
    public Disposable getCollect(int userId, String sign, String topic, int appid, int sentenceFlg,
                                 MyCollectContract.CollectCallback collectCallback) {

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
    public Disposable getCollect_wx(String userId, String groupName, String type, int sentenceFlg,
                                    int pageNumber, int pageCounts, MyCollectContract.NCollectCallback nCollectCallback) {

        return NetWorkManager
                .getRequestForApps()
                .getCollect_wx(userId, groupName, type, sentenceFlg, pageNumber, pageCounts)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewCollectBean>() {
                    @Override
                    public void accept(NewCollectBean newCollectBean) throws Exception {

                        nCollectCallback.success(newCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        nCollectCallback.error((Exception) throwable);
                    }
                });
    }
}
