package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.entity.DubbingPublish;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingPublishBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.view.dubbing.DubbingPreviewContract;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DubbingPreviewModel implements DubbingPreviewContract.DubbingPreviewModel {


    @Override
    public Disposable publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish,
                                     DubbingPreviewContract.Callback callback) {

        return NetWorkManager
                .getRequestForVoa()
                .publishDubbing(protocol, content, userid, dubbingPublish)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DubbingPublishBean>() {
                    @Override
                    public void accept(DubbingPublishBean dubbingPublishBean) throws Exception {

                        callback.success(dubbingPublishBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile, TextbookDetailsContract.UpdateScoreCallback updateScoreCallback) {

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
}
