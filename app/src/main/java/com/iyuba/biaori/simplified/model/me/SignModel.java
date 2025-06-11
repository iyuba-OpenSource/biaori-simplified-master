package com.iyuba.biaori.simplified.model.me;


import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.me.MyTimeBean;
import com.iyuba.biaori.simplified.model.bean.me.SignBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.view.me.SignContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignModel implements SignContract.SignModel {

    @Override
    public Disposable getMyTime(String uid, long day, int flg, SignContract.MyTimeCallback myTimeCallback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getMyTime(uid, day, flg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyTimeBean>() {
                    @Override
                    public void accept(MyTimeBean myTimeBean) throws Exception {

                        myTimeCallback.success(myTimeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        myTimeCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile, SignContract.SignCallback signCallback) {

        return NetWorkManager
                .getRequestForApiCn()
                .updateScore(flag, uid, appid, idindex, srid, mobile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateScoreBean>() {
                    @Override
                    public void accept(UpdateScoreBean updateScoreBean) throws Exception {

                        signCallback.success(updateScoreBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        signCallback.error((Exception) throwable);
                    }
                });
    }

}
