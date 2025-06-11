package com.iyuba.biaori.simplified.view.me;



import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.me.RewardBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MyWalletContract {

    interface MyWalletView extends LoadingView {


        void wallet(int pages, List<RewardBean.DataDTO> dataDTOS);
    }

    interface MyWalletPresenter extends IBasePresenter<MyWalletView> {

        void getUserActionRecord(int uid, int pages, int pageCount, String sign);
    }


    interface MyWalletModel extends BaseModel {

        Disposable getUserActionRecord(int uid, int pages, int pageCount, String sign, WalletCallback walletCallback);
    }

    interface WalletCallback {

        void success(RewardBean rewardBean);

        void error(Exception e);
    }
}
