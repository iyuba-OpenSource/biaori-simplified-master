package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.WordCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface WordDetailsContract {

    interface WordDetailsView extends LoadingView {

        void jtestComplete(EvaluteRecordBean evaluteRecordBean);

        void updateCollectComplete(String wordid, String type);
    }


    interface WordDetailsPresenter extends IBasePresenter<WordDetailsView> {

        void jtest(int userId, int IdIndex, int paraId, int newsId, String type, String filePath, String sentence);

        void updateWordCollect(String userid, String wordid,
                               String type, String appid);
    }

    interface WordDetailsModel extends BaseModel {

        Disposable jtest(Map<String, RequestBody> params, EvaluteRecordCallback evaluteRecordCallback);

        Disposable updateWordCollect(String userid, String wordid,
                                     String type, String appid, WordCollectCallback wordCollectCallback);
    }

    interface WordCollectCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);
    }

    interface EvaluteRecordCallback {

        void success(EvaluteRecordBean evaluteRecordBean);

        void error(Exception e);
    }
}
