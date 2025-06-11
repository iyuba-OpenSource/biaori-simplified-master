package com.iyuba.biaori.simplified.presenter.me;



import com.iyuba.biaori.simplified.model.bean.me.RewardBean;
import com.iyuba.biaori.simplified.model.me.MyWalletModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.MyWalletContract;

import io.reactivex.disposables.Disposable;

public class MyWalletPresenter extends BasePresenter<MyWalletContract.MyWalletView, MyWalletContract.MyWalletModel>
        implements MyWalletContract.MyWalletPresenter {


    @Override
    protected MyWalletContract.MyWalletModel initModel() {
        return new MyWalletModel();
    }

    @Override
    public void getUserActionRecord(int uid, int pages, int pageCount, String sign) {

        Disposable disposable = model.getUserActionRecord(uid, pages, pageCount, sign, new MyWalletContract.WalletCallback() {

            @Override
            public void success(RewardBean rewardBean) {

                if (rewardBean.getResult() == 200) {

                    view.wallet(pages, rewardBean.getData());
                }
            }

            @Override
            public void error(Exception e) {

                view.wallet(pages, null);
            }
        });
        addSubscribe(disposable);
    }
}
