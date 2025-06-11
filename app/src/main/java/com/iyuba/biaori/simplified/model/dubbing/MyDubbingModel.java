package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.DelEvalBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.MyDubbingBean;
import com.iyuba.biaori.simplified.view.dubbing.MyDubbingContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyDubbingModel implements MyDubbingContract.MyDubbingModel {


    @Override
    public Disposable getTalkShowOtherWorks(int appid, String uid, String appname, MyDubbingContract.Callback callback) {

        return NetWorkManager
                .getRequestForVoa()
                .getTalkShowOtherWorks(appid, uid, appname)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyDubbingBean>() {
                    @Override
                    public void accept(MyDubbingBean myDubbingBean) throws Exception {

                        callback.success(myDubbingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable delEvalAndDubbing(int protocol, int id, MyDubbingContract.DelCallback delCallback) {

        return NetWorkManager
                .getRequestForVoa()
                .delEvalAndDubbing(protocol, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DelEvalBean>() {
                    @Override
                    public void accept(DelEvalBean delEvalBean) throws Exception {

                        delCallback.success(delEvalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        delCallback.error((Exception) throwable);
                    }
                });
    }
}
