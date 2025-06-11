package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PublishEvalBean;
import com.iyuba.biaori.simplified.model.textbook.EvaluatingModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.EvaluatingContract;

import java.net.UnknownHostException;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class EvaluatingPresenter extends BasePresenter<EvaluatingContract.EvaluatingView, EvaluatingContract.EvaluatingModel>
        implements EvaluatingContract.EvaluatingPresenter {


    @Override
    protected EvaluatingContract.EvaluatingModel initModel() {
        return new EvaluatingModel();
    }


    @Override
    public void jtest(RequestBody body, String idindex) {


        Disposable disposable = model.jtest(body, new EvaluatingContract.EvalCallback() {
            @Override
            public void success(EvaluteRecordBean evaluteRecordBean) {

                if (evaluteRecordBean.getResult().equals("1")) {

                    view.jtestComplete(evaluteRecordBean, idindex);
                } else {

                    view.toast(evaluteRecordBean.getMessage());
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
    public void jtest(Map<String, RequestBody> params, String idindex) {


        Disposable disposable = model.jtest(params, new EvaluatingContract.EvalCallback() {
            @Override
            public void success(EvaluteRecordBean evaluteRecordBean) {

                if (evaluteRecordBean.getResult().equals("1")) {

                    view.jtestComplete(evaluteRecordBean, idindex);
                } else {

                    view.toast(evaluteRecordBean.getMessage());
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
    public void publishEval(String topic, String content, String format, int idIndex, int paraid,
                            String platform, int protocol, int score, int shuoshuotype, String userid,
                            String name, String voaid) {

        Disposable disposable = model.publishEval(topic, content, format, idIndex, paraid, platform,
                protocol, score, shuoshuotype, userid, name, voaid, new EvaluatingContract.PublishCallback() {
                    @Override
                    public void success(PublishEvalBean publishEvalBean) {


                        view.showReward(publishEvalBean.getReward());
                        if (publishEvalBean.getResultCode().equals("501")) {

                            view.getPublishResult(publishEvalBean, idIndex + "", shuoshuotype);
//                            view.toast("获得积分" + publishEvalBean.getAddScore());
                            view.toast("上传成功");
                        } else {

                            view.toast(publishEvalBean.getMessage());
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
    public void merge(String audios, String type) {

        Disposable disposable = model.merge(audios, type, new EvaluatingContract.MergeCallback() {

            @Override
            public void success(MergeBean mergeBean) {

                if (mergeBean.getResult().equals("1")) {

                    view.getMerge(mergeBean);
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
