package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PublishEvalBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.PartMap;

public interface EvaluatingContract {


    interface EvaluatingView extends LoadingView {

        void jtestComplete(EvaluteRecordBean evaluteRecordBean, String idindex);

        /**
         * @param publishEvalBean
         * @param idindex
         * @param shuoshuotype    用来区分是合成发布还是单据发布
         */
        void getPublishResult(PublishEvalBean publishEvalBean, String idindex, int shuoshuotype);


        void getMerge(MergeBean mergeBean);

        void showReward(String rewardStr);
    }

    interface EvaluatingPresenter extends IBasePresenter<EvaluatingView> {

        void jtest(RequestBody body, String idindex);

        void jtest(Map<String, RequestBody> params, String idindex);

        void publishEval(String topic, String content, String format, int idIndex, int paraid, String platform,
                         int protocol, int score, int shuoshuotype, String userid, String name,
                         String voaid);

        void merge(String audios, String type);
    }

    interface EvaluatingModel extends BaseModel {

        Disposable jtest(RequestBody body, EvalCallback callback);


        Disposable jtest(Map<String, RequestBody> params, EvalCallback callback);

        Disposable publishEval(String topic, String content, String format, int idIndex, int paraid, String platform,
                               int protocol, int score, int shuoshuotype, String userid, String name,
                               String voaid, PublishCallback callback);

        Disposable merge(String audios, String type, MergeCallback mergeCallback);
    }


    interface MergeCallback {

        void success(MergeBean mergeBean);

        void error(Exception e);
    }

    interface PublishCallback {

        void success(PublishEvalBean publishEvalBean);

        void error(Exception e);
    }

    interface EvalCallback {

        void success(EvaluteRecordBean evaluteRecordBean);

        void error(Exception e);
    }
}
