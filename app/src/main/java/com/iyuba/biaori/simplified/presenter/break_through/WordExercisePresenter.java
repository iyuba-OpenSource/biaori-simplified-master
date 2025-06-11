package com.iyuba.biaori.simplified.presenter.break_through;

import com.iyuba.biaori.simplified.model.break_through.WordExerciseModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.break_through.WordExerciseContract;

public class WordExercisePresenter extends BasePresenter<WordExerciseContract.WordAnswerView, WordExerciseContract.WordAnswerModel>
        implements WordExerciseContract.WordAnswerPresenter {
    @Override
    protected WordExerciseContract.WordAnswerModel initModel() {
        return new WordExerciseModel();
    }
}
