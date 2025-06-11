package com.iyuba.biaori.simplified.model.me;

import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.SyncListenBean;
import com.iyuba.biaori.simplified.model.bean.login.LogoffBean;
import com.iyuba.biaori.simplified.model.bean.me.GroupBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncEvalBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncWordBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.view.me.MeContract;
import com.iyuba.biaori.simplified.view.vip.VipContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MeModel implements MeContract.MeModel {


    @Override
    public Disposable logoff(int protocol, String username, String password, String format,
                             String sign, MeContract.LogoffCallback logoffCallback) {

        return NetWorkManager
                .getRequest()
                .logoff(protocol, username, password, format, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LogoffBean>() {
                    @Override
                    public void accept(LogoffBean logoffBean) throws Exception {

                        logoffCallback.success(logoffBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        logoffCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage, int TestMode,
                                               String sign, String Lesson, MeContract.ListenCallback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getStudyRecordByTestMode(format, uid, Pageth, NumPerPage, TestMode, sign, Lesson)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncListenBean>() {
                    @Override
                    public void accept(SyncListenBean syncListenBean) throws Exception {

                        callback.success(syncListenBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTestRecord(String userId, String newstype, MeContract.EvalCallback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getTestRecord(userId, newstype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncEvalBean>() {
                    @Override
                    public void accept(SyncEvalBean syncEvalBean) throws Exception {

                        callback.success(syncEvalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getExamDetail(int appId, String uid, String lesson, String TestMode, int mode,
                                    String sign, String format, MeContract.WordCallback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .getExamDetail(appId, uid, lesson, TestMode, mode, sign, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncWordBean>() {
                    @Override
                    public void accept(SyncWordBean syncWordBean) throws Exception {

                        callback.success(syncWordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getQQGroup(String type, VipContract.JpQQ2Callback callback) {

        return NetWorkManager
                .getRequestForM()
                .getQQGroup(type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean2>() {
                    @Override
                    public void accept(JpQQBean2 jpQQBean2) throws Exception {

                        callback.success(jpQQBean2);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getJpQQ(int appid, VipContract.JpQQCallback callback) {

        return NetWorkManager
                .getRequestForAI()
                .getJpQQ(appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean>() {
                    @Override
                    public void accept(JpQQBean jpQQBean) throws Exception {

                        callback.success(jpQQBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable entergroup(String uid, String apptype, MeContract.Callback callback) {

        return NetWorkManager
                .getRequestForAI()
                .entergroup(uid, apptype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GroupBean>() {
                    @Override
                    public void accept(GroupBean groupBean) throws Exception {

                        callback.success(groupBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
