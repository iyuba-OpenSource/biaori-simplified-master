package com.iyuba.biaori.simplified.view.dubbing;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface DubbingContract {

    interface DubbingView extends LoadingView {

        void textExamApiComplete(DubbingTextBean dubbingTextBean);

        void jtestComplete(EvaluteRecordBean evaluteRecordBean, int paraId);
    }

    interface DubbingPresenter extends IBasePresenter<DubbingView> {

        void textExamApi(String format, String voaid);

        void jtest(int userId, int IdIndex, int paraId, int newsId, String type, String filePath, String sentence);
    }

    interface DubbingModel extends BaseModel {

        Disposable textExamApi(String format, String voaid, Callback callback);

        Disposable jtest(Map<String, RequestBody> params, TestCallback callback);
    }

    interface Callback {

        void success(DubbingTextBean dubbingTextBean);

        void error(Exception e);
    }

    interface TestCallback {

        void success(EvaluteRecordBean evaluteRecordBean);

        void error(Exception e);
    }
}
