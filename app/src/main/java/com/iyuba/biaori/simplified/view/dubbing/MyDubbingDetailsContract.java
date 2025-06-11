package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

public interface MyDubbingDetailsContract {

    interface MyDubbingDetailsView extends LoadingView {


    }

    interface MyDubbingDetailsPresenter extends IBasePresenter<MyDubbingDetailsView> {


    }

    interface MyDubbingDetailsModel extends BaseModel {


    }
}
