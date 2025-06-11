package com.iyuba.biaori.simplified.view.me;


import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.me.MyTimeBean;
import com.iyuba.biaori.simplified.model.bean.me.SignBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface SignContract {

    interface SignView extends LoadingView {

        /**
         * 获取学习的时间
         *
         * @param myTimeBean
         */
        void getMyTime(MyTimeBean myTimeBean);

        /**
         * 减少积分
         *
         * @param signBean
         */
        void updateScoreComplete(UpdateScoreBean updateScoreBean);
    }

    interface SignPresenter extends IBasePresenter<SignView> {

        void getMyTime(String uid, long day, int flg);

        void updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile);
    }


    interface SignModel extends BaseModel {

        Disposable getMyTime(String uid, long day, int flg, MyTimeCallback myTimeCallback);

        Disposable updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile, SignCallback signCallback);
    }

    interface SignCallback {

        void success(UpdateScoreBean updateScoreBean);

        void error(Exception e);
    }

    interface MyTimeCallback {

        void success(MyTimeBean myTimeBean);

        void error(Exception e);
    }
}
