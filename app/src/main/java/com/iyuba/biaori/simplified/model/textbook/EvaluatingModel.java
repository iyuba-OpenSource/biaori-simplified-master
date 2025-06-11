package com.iyuba.biaori.simplified.model.textbook;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PublishEvalBean;
import com.iyuba.biaori.simplified.view.textbook.EvaluatingContract;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class EvaluatingModel implements EvaluatingContract.EvaluatingModel {


    @Override
    public Disposable jtest(RequestBody body, EvaluatingContract.EvalCallback callback) {

        return NetWorkManager
                .getRequestForIUser()
                .jtest(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvaluteRecordBean>() {
                    @Override
                    public void accept(EvaluteRecordBean evaluteRecordBean) throws Exception {

                        callback.success(evaluteRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable jtest(Map<String, RequestBody> params, EvaluatingContract.EvalCallback callback) {

        return NetWorkManager
                .getRequestForIUser()
                .jtest(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvaluteRecordBean>() {
                    @Override
                    public void accept(EvaluteRecordBean evaluteRecordBean) throws Exception {

                        callback.success(evaluteRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable publishEval(String topic, String content, String format, int idIndex, int paraid,
                                  String platform, int protocol, int score, int shuoshuotype, String userid,
                                  String name, String voaid, EvaluatingContract.PublishCallback callback) {

        return NetWorkManager
                .getRequestForVoa()
                .publishEval(topic, content, format, idIndex, paraid, platform, protocol, score,
                        shuoshuotype, userid, name, voaid, "1", Constant.APPID + "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PublishEvalBean>() {
                    @Override
                    public void accept(PublishEvalBean publishEvalBean) throws Exception {

                        callback.success(publishEvalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable merge(String audios, String type, EvaluatingContract.MergeCallback mergeCallback) {

        return NetWorkManager
                .getRequestForIUser()
                .merge(audios, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MergeBean>() {
                    @Override
                    public void accept(MergeBean mergeBean) throws Exception {

                        mergeCallback.success(mergeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mergeCallback.error((Exception) throwable);
                    }
                });
    }
}
