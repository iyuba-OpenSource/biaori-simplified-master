package com.iyuba.biaori.simplified.view.break_through;

import com.iyuba.biaori.simplified.entity.ExamRecordPost;
import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.break_through.ExamRecordBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

public interface WordAnswerContract {


    interface WordAnswerView extends LoadingView {

        void updateExamRecordComplete(ExamRecordBean examRecordBean);
    }

    interface WordAnswerPresenter extends IBasePresenter<WordAnswerView> {


        void updateExamRecord(ExamRecordPost bean);

    }


    interface WordAnswerModel extends BaseModel {

        Disposable updateExamRecord(ExamRecordPost bean, Callback callback);
    }

    interface Callback {

        void success(ResponseBody examRecordBean);

        void error(Exception e);
    }
}
