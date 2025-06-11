package com.iyuba.biaori.simplified.presenter.me;

import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.NewCollectBean;
import com.iyuba.biaori.simplified.model.me.MyCollectModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.MyCollectContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MyCollectPresenter extends BasePresenter<MyCollectContract.MyCollectView, MyCollectContract.MyCollectModel>
        implements MyCollectContract.MyCollectPresenter {


    @Override
    protected MyCollectContract.MyCollectModel initModel() {
        return new MyCollectModel();
    }

    @Override
    public void getCollect(int userId, String sign, String topic, int appid, int sentenceFlg) {

        Disposable disposable = model.getCollect(userId, sign, topic, appid, sentenceFlg, new MyCollectContract.CollectCallback() {
            @Override
            public void success(CollectBean collectBean) {

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
    public void getCollect_wx(String userId, String groupName, String type, int sentenceFlg, int pageNumber, int pageCounts) {

        Disposable disposable = model.getCollect_wx(userId, groupName, type, sentenceFlg, pageNumber, pageCounts, new MyCollectContract.NCollectCallback() {
            @Override
            public void success(NewCollectBean newCollectBean) {

                if (newCollectBean.getResult().equals("200")) {

                    view.getCollectComplete(newCollectBean);
                }else{
                    view.hideLoading();
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
