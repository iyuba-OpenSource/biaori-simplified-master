package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface LessonWordContract {

    interface LessonWordView extends LoadingView {

        void getJpWordSentenceComplete(JpWordSentenceBean jpWordSentenceBean);
    }


    interface LessonWordPresenter extends IBasePresenter<LessonWordView> {

        void getJpWordSentence(int level, int sourceid);
    }

    interface LessonWordModel extends BaseModel {

        Disposable getJpWordSentence(int level, int sourceid, JpWordSentenceCallback jpWordSentenceCallback);
    }

    interface JpWordSentenceCallback {

        void success(JpWordSentenceBean jpWordSentenceBean);

        void error(Exception e);

    }


}
