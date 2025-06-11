package com.iyuba.biaori.simplified.view.me;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.SyncListenBean;
import com.iyuba.biaori.simplified.model.bean.login.LogoffBean;
import com.iyuba.biaori.simplified.model.bean.me.GroupBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncEvalBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncWordBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;
import com.iyuba.biaori.simplified.view.vip.VipContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MeContract {

    interface MeView extends LoadingView {

        void logoffComplete(LogoffBean logoffBean);

        void hideProgressDialog();

        void showQQDialog(JpQQBean jpQQBean, JpQQBean2 jpQQBean2);
    }

    interface MePresenter extends IBasePresenter<MeView> {

        void logoff(int protocol, String username, String password,
                    String format, String sign);

        void getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage, int TestMode,
                                      String sign, String Lesson);

        void getTestRecord(String userId, String newstype);

        void getExamDetail(int appId, String uid, String lesson, String TestMode, int mode, String sign,
                           String format);


        void getQQGroup(String type);

        void getJpQQ(int appid, JpQQBean2 jpQQBean2);

        void entergroup(String uid, String apptype);

    }

    interface MeModel extends BaseModel {

        Disposable logoff(int protocol, String username, String password,
                          String format, String sign, LogoffCallback logoffCallback);


        Disposable getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage, int TestMode,
                                            String sign, String Lesson, ListenCallback callback);


        Disposable getTestRecord(String userId, String newstype, EvalCallback callback);


        Disposable getExamDetail(int appId, String uid, String lesson, String TestMode, int mode, String sign,
                                 String format, WordCallback callback);

        Disposable getQQGroup(String type, VipContract.JpQQ2Callback callback);

        Disposable getJpQQ(int appid, VipContract.JpQQCallback callback);

        Disposable entergroup(String uid, String apptype, Callback callback);
    }


    interface Callback {

        void success(GroupBean groupBean);

        void error(Exception e);
    }


    interface WordCallback {

        void success(SyncWordBean syncWordBean);

        void error(Exception e);
    }

    interface EvalCallback {

        void success(SyncEvalBean syncEvalBean);

        void error(Exception e);
    }

    interface ListenCallback {

        void success(SyncListenBean syncListenBean);

        void error(Exception e);
    }

    interface LogoffCallback {

        void success(LogoffBean logoffBean);

        void error(Exception e);
    }
}
