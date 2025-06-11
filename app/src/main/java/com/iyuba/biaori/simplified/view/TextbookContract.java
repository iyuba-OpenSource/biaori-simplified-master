package com.iyuba.biaori.simplified.view;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.JpLessonBean;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface TextbookContract {

    interface TextbookView extends LoadingView {

        void getJpLessonComplete(JpLessonBean jpLessonBean);

        void getCollectComplete(CollectBean collectBean);

        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }

    interface TextbookPresenter extends IBasePresenter<TextbookView> {

        void getJpLesson(String source);

        void getCollect(int userId, String sign, String topic,
                        int appid, int sentenceFlg);

        void getAdEntryAll(String appId, int flag, String uid);
    }

    interface TextbookModel extends BaseModel {


        Disposable getJpLesson(String source, JpLessonCallback jpLessonCallback);

        Disposable getCollect(int userId, String sign, String topic,
                              int appid, int sentenceFlg, CollectCallback collectCallback);

        Disposable getAdEntryAll(String appId, int flag, String uid, ADCallback callback);
    }


    interface ADCallback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }

    interface CollectCallback {

        void success(CollectBean collectBean);

        void error(Exception e);
    }

    interface JpLessonCallback {

        void success(JpLessonBean jpLessonBean);

        void error(Exception e);

    }

}
