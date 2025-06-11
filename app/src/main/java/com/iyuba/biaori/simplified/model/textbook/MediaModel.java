package com.iyuba.biaori.simplified.model.textbook;


import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.view.textbook.MediaContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MediaModel implements MediaContract.MediaModel {


    @Override
    public Disposable updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                           String Lesson, String TestMode, String TestWords, String platform,
                                           String appName, String DeviceId, String LessonId, String sign,
                                           int EndFlg, int TestNumber, MediaContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .updateStudyRecordNew(format, uid, BeginTime, EndTime, Lesson, TestMode, TestWords, platform,
                        appName, DeviceId, LessonId, sign, EndFlg, TestNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        callback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

}
