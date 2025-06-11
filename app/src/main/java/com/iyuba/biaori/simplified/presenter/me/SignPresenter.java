package com.iyuba.biaori.simplified.presenter.me;


import com.iyuba.biaori.simplified.model.bean.me.MyTimeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.model.me.SignModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.SignContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class SignPresenter extends BasePresenter<SignContract.SignView, SignContract.SignModel>
        implements SignContract.SignPresenter {


    @Override
    public void getMyTime(String uid, long day, int flg) {

        Disposable disposable = model.getMyTime(uid, day, flg, new SignContract.MyTimeCallback() {
            @Override
            public void success(MyTimeBean myTimeBean) {

                view.hideLoading();
                if (myTimeBean.getResult().equals("1")) {

                    view.getMyTime(myTimeBean);
                } else {

                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时！");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile) {

        Disposable disposable = model.updateScore(flag, uid, appid, idindex, srid, mobile, new SignContract.SignCallback() {
            @Override
            public void success(UpdateScoreBean updateScoreBean) {

                if (updateScoreBean.getResult().equals("200")) {

                    view.updateScoreComplete(updateScoreBean);
                } else {

                    view.toast("您今日已打卡");
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }


    @Override
    protected SignContract.SignModel initModel() {
        return new SignModel();
    }
}
