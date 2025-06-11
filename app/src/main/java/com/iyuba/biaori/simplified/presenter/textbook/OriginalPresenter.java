package com.iyuba.biaori.simplified.presenter.textbook;

import android.util.Log;

import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;
import com.iyuba.biaori.simplified.model.textbook.OriginalModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.util.XmlUtil;
import com.iyuba.biaori.simplified.view.WelcomeContract;
import com.iyuba.biaori.simplified.view.textbook.OriginalContract;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OriginalPresenter extends BasePresenter<OriginalContract.OriginalView, OriginalContract.OriginalModel>
        implements OriginalContract.OriginalPresenter {

    @Override
    protected OriginalContract.OriginalModel initModel() {
        return new OriginalModel();
    }

    @Override
    public void getJpSentence(String name, String sourceid) {

        Disposable disposable = model.getJpSentence(name, sourceid, new OriginalContract.JpSentenceCallback() {
            @Override
            public void success(JpSentenceBean jpSentenceBean) {

                if (jpSentenceBean.getResult() == 200) {

                    view.getJpSentenceComplete(jpSentenceBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.getJpSentenceComplete(null);
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateCollect(String groupName, int sentenceFlg, int appId, int userId, String type, int voaId, int sentenceId, String topic) {

        Disposable disposable = model.updateCollect(groupName, sentenceFlg, appId, userId, type,
                voaId, sentenceId, topic, new OriginalContract.UpdateCollectCallback() {
                    @Override
                    public void success(ResponseBody responseBody) {

//                        String resultStr = null;
//                        try {
//                            resultStr = responseBody.string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if (resultStr != null) {
//
//                            UpdateCollectBean updateCollectBean = XmlUtil.parseXMLWithPull(resultStr.trim());
//
//                            view.updateCollectComplete(updateCollectBean);
//                        }
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
    public void updateCollect_wx(String userId, String voaId, int sentenceId, String groupName, int sentenceFlg,
                                 String type, String topic) {

        Disposable disposable = model.updateCollect_wx(userId, voaId, sentenceId, groupName, sentenceFlg
                , type, topic, new OriginalContract.SCollectCallback() {
                    @Override
                    public void success(UCollectBean uCollectBean) {

                        if (uCollectBean.getResult().equals("200")) {

                            view.updateCollectComplete(uCollectBean, type);
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
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new OriginalContract.AdCallback() {
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
