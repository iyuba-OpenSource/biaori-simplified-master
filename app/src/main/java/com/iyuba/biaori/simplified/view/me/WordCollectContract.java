package com.iyuba.biaori.simplified.view.me;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.me.WordPdfBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordCollectBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface WordCollectContract {

    interface WordCollectView extends LoadingView {

        void getWordToPDF(WordPdfBean wordPdfBean);

        void getWordCollected(JpWordCollectBean wordCollectBean);
    }


    interface WordCollectPresenter extends IBasePresenter<WordCollectView> {

        void getWordToPDF(int userid, String type, String ntype);
        void getWordCollect(String userid, String type, int appid);
    }

    interface WordCollectModel extends BaseModel {

        Disposable getWordCollect(String userid, String type, int appid, Callback callback);

        Disposable getWordToPDF(int userid, String type, String ntype, WordPdfCallback WordAllListModel);
    }

    interface Callback {

        void success(JpWordCollectBean jpWordCollectBean);

        void error(Exception e);
    }


    interface WordPdfCallback {

        void success(WordPdfBean wordPdfBean);

        void error(Exception e);
    }
}
