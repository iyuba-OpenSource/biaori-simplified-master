package com.iyuba.biaori.simplified.presenter.dubbing;

import com.iyuba.biaori.simplified.model.bean.dubbing.DelEvalBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.MyDubbingBean;
import com.iyuba.biaori.simplified.model.dubbing.MyDubbingModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.MyDubbingContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MyDubbingPresenter extends BasePresenter<MyDubbingContract.MyDubbingView, MyDubbingContract.MyDubbingModel>
        implements MyDubbingContract.MyDubbingPresenter {

    @Override
    protected MyDubbingContract.MyDubbingModel initModel() {
        return new MyDubbingModel();
    }

    @Override
    public void getTalkShowOtherWorks(int appid, String uid, String appname) {

        Disposable disposable = model.getTalkShowOtherWorks(appid, uid, appname, new MyDubbingContract.Callback() {
            @Override
            public void success(MyDubbingBean myDubbingBeans) {

                if (myDubbingBeans.isResult()) {

                    view.getMyDubbingComplete(myDubbingBeans);
                } else {

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
    public void delEvalAndDubbing(int protocol, int id) {

        Disposable disposable = model.delEvalAndDubbing(protocol, id, new MyDubbingContract.DelCallback() {
            @Override
            public void success(DelEvalBean delEvalBean) {

                if (delEvalBean.getResultCode().equals("001")) {

                    view.delEvalComplete(id);
                } else {

                    view.toast(delEvalBean.getMessage());
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
}
