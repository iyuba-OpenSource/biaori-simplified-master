package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;
import com.iyuba.biaori.simplified.view.WelcomeContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

public interface OriginalContract {

    interface OriginalView extends LoadingView {

        void getJpSentenceComplete(JpSentenceBean jpSentenceBean);

        /**
         * 是取消收藏还是收藏
         *
         * @param type
         * @param updateCollectBean
         */
        void updateCollectComplete(UCollectBean uCollectBean, String type);


        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }


    interface OriginalPresenter extends IBasePresenter<OriginalView> {

        void getJpSentence(String name, String sourceid);

        void updateCollect(String groupName, int sentenceFlg, int appId,
                           int userId, String type, int voaId,
                           int sentenceId, String topic);

        void updateCollect_wx(String userId, String voaId, int sentenceId
                , String groupName, int sentenceFlg, String type
                , String topic);

        void getAdEntryAll(String appId, int flag, String uid);
    }

    interface OriginalModel extends BaseModel {

        Disposable getJpSentence(String name, String sourceid, JpSentenceCallback jpSentenceCallback);

        Disposable updateCollect(String groupName, int sentenceFlg, int appId,
                                 int userId, String type, int voaId,
                                 int sentenceId, String topic, UpdateCollectCallback updateCollectCallback);

        Disposable updateCollect_wx(String userId, String voaId, int sentenceId
                , String groupName, int sentenceFlg, String type
                , String topic, SCollectCallback sCollectCallback);

        Disposable getAdEntryAll(String appId, int flag, String uid, AdCallback callback);
    }


    interface AdCallback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }

    interface SCollectCallback {

        void success(UCollectBean uCollectBean);

        void error(Exception e);
    }

    interface UpdateCollectCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface JpSentenceCallback {

        void success(JpSentenceBean jpSentenceBean);

        void error(Exception e);
    }
}

