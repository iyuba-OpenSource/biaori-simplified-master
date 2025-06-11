package com.iyuba.biaori.simplified.model.break_through;

import com.iyuba.biaori.simplified.entity.ExamRecordPost;
import com.iyuba.biaori.simplified.model.NetWorkManager;
import com.iyuba.biaori.simplified.model.bean.break_through.ExamRecordBean;
import com.iyuba.biaori.simplified.view.break_through.WordAnswerContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class WordAnswerModel implements WordAnswerContract.WordAnswerModel {


    @Override
    public Disposable updateExamRecord(ExamRecordPost examRecordPost, WordAnswerContract.Callback callback) {

        return NetWorkManager
                .getRequestForDaXue()
                .updateExamRecord(examRecordPost)
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
