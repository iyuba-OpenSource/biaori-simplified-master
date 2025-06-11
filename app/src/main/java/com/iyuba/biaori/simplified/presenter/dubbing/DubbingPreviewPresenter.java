package com.iyuba.biaori.simplified.presenter.dubbing;

import com.google.gson.Gson;
import com.iyuba.biaori.simplified.entity.DubbingPublish;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingPublishBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.model.dubbing.DubbingPreviewModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.DubbingPreviewContract;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class DubbingPreviewPresenter extends BasePresenter<DubbingPreviewContract.DubbingPreviewView, DubbingPreviewContract.DubbingPreviewModel>
        implements DubbingPreviewContract.DubbingPreviewPresenter {

    @Override
    protected DubbingPreviewContract.DubbingPreviewModel initModel() {
        return new DubbingPreviewModel();
    }

    @Override
    public void publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish) {

        Disposable disposable = model.publishDubbing(protocol, content, userid, dubbingPublish, new DubbingPreviewContract.Callback() {
            @Override
            public void success(DubbingPublishBean dubbingPublishBean) {

                if (dubbingPublishBean.getResultCode() == 200) {

                    view.toast("发布成功");
                    view.publishDubbingCompelte(dubbingPublishBean);
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
    public void updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile) {

        Disposable disposable = model.updateScore(flag, uid, appid, idindex, srid, mobile, new TextbookDetailsContract.UpdateScoreCallback() {
            @Override
            public void success(UpdateScoreBean updateScoreBean) {

                if (updateScoreBean.getResult().equals("200")) {

                    view.updateScore(updateScoreBean);
                    view.toast("扣除积分成功");
                } else {

                    view.toast(updateScoreBean.getMessage());
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
