package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.model.textbook.LessonWordModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.LessonWordContract;

import io.reactivex.disposables.Disposable;

public class LessonWordPresenter extends BasePresenter<LessonWordContract.LessonWordView, LessonWordContract.LessonWordModel>
        implements LessonWordContract.LessonWordPresenter {


    @Override
    protected LessonWordContract.LessonWordModel initModel() {
        return new LessonWordModel();
    }

    @Override
    public void getJpWordSentence(int level, int sourceid) {

        Disposable disposable = model.getJpWordSentence(level, sourceid, new LessonWordContract.JpWordSentenceCallback() {
            @Override
            public void success(JpWordSentenceBean jpWordSentenceBean) {

                view.hideLoading();
                if (jpWordSentenceBean.getResult() == 200) {

                    view.getJpWordSentenceComplete(jpWordSentenceBean);
                }else{

                    view.getJpWordSentenceComplete(null);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                view.getJpWordSentenceComplete(null);
            }
        });
        addSubscribe(disposable);
    }
}
