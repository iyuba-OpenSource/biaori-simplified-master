package com.iyuba.biaori.simplified.model.me;



import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.view.me.MyWalletContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyWalletModel implements MyWalletContract.MyWalletModel {


    @Override
    public Disposable getUserActionRecord(int uid, int pages, int pageCount, String sign, MyWalletContract.WalletCallback walletCallback) {

        return NetWorkManager
                .getRequestForApiCn()
                .getUserActionRecord(Constant.GET_USER_ACTION_RECORD, uid, pages, pageCount, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletCallback::success, throwable -> {

                    walletCallback.error((Exception) throwable);
                });
    }
}
