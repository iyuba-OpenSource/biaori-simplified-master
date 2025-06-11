package com.iyuba.biaori.simplified.presenter;

import com.iyuba.biaori.simplified.model.WelcomeModel;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.view.WelcomeContract;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class WelcomePresenter extends BasePresenter<WelcomeContract.WelcomeView, WelcomeContract.WelcomeModel>
        implements WelcomeContract.WelcomePresenter {


    @Override
    protected WelcomeContract.WelcomeModel initModel() {
        return new WelcomeModel();
    }

    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new WelcomeContract.Callback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeans) {

                if (adEntryBeans.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeans.get(0);
                    if (adEntryBean.getResult().equals("1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }
}
