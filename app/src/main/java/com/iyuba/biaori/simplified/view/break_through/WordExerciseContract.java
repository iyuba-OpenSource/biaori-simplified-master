package com.iyuba.biaori.simplified.view.break_through;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

public interface WordExerciseContract {


    interface WordAnswerView extends LoadingView {

    }

    interface WordAnswerPresenter extends IBasePresenter<WordAnswerView> {


    }


    interface WordAnswerModel extends BaseModel {

    }
}

