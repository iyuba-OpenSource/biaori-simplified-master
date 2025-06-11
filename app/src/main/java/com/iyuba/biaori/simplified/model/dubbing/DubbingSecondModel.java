package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.view.dubbing.DubbingSecondContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DubbingSecondModel implements DubbingSecondContract.DubbingSecondModel {


    @Override
    public Disposable getTitleBySeriesid(String type, int seriesid, String sign, String format,
                                         DubbingSecondContract.Callback callback) {

        return NetWorkManager
                .getRequestForCms()
                .getTitleBySeriesid(type, seriesid, sign, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SeriesBean>() {
                    @Override
                    public void accept(SeriesBean seriesBean) throws Exception {

                        callback.success(seriesBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
