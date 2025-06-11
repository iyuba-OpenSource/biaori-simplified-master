package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.SeriesBean;
import com.iyuba.biaori.simplified.view.dubbing.DubbingListContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DubbingListModel implements DubbingListContract.DubbingListModel {


    @Override
    public Disposable getTitleBySeries(String type, int category, String sign, String format,
                                       DubbingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForCms()
                .getTitleBySeries(type, category, sign, format)
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

    @Override
    public Disposable titleJpPeiYinApi(String type, String format, int pages, int pageNum,
                                       int maxid, int parentID, DubbingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForApps()
                .titleJpPeiYinApi(type, format, pages, pageNum, maxid, parentID)
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
