package com.iyuba.biaori.simplified.model.dubbing;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingRankBean;
import com.iyuba.biaori.simplified.view.dubbing.DubbingRankContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DubbingRankModel implements DubbingRankContract.DubbingRankModel {


    @Override
    public Disposable getDubbingRank(String platform, String format, int protocol, String voaid,
                                     int pageNumber, int pageCounts, int sort, String topic, int selectType,
                                     DubbingRankContract.Callback callback) {

        return NetWorkManager
                .getRequestForVoa()
                .getDubbingRank(platform, format, protocol, voaid, pageNumber, pageCounts, sort, topic, selectType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DubbingRankBean>() {
                    @Override
                    public void accept(DubbingRankBean dubbingRankBean) throws Exception {

                        callback.success(dubbingRankBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
