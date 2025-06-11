package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.view.dubbing.DubbingContract;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DubbingModel implements DubbingContract.DubbingModel {


    @Override
    public Disposable textExamApi(String format, String voaid, DubbingContract.Callback callback) {

        return NetWorkManager
                .getRequestForApps()
                .textExamApi(format, voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DubbingTextBean>() {
                    @Override
                    public void accept(DubbingTextBean dubbingTextBean) throws Exception {

                        callback.success(dubbingTextBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable jtest(Map<String, RequestBody> params, DubbingContract.TestCallback callback) {

        return NetWorkManager
                .getRequestForIUser()
                .jtest(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvaluteRecordBean>() {
                    @Override
                    public void accept(EvaluteRecordBean evaluteRecordBean) throws Exception {

                        callback.success(evaluteRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
