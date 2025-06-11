package com.iyuba.biaori.simplified.view.textbook;

import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PdfFileBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface TextbookDetailsContract {


    interface TextbookDetailsView extends LoadingView {

        void getpdf(PdfFileBean pdfFileBean);

        void updateScore(UpdateScoreBean updateScoreBean, int srid);

        /**
         * 发送pdf到朋友圈
         *
         * @param pdfFileBean
         */
        void sendPdfToQQOrWX(PdfFileBean pdfFileBean);

        void showPdfDialog(PdfFileBean pdfFileBean);

        void getJpSentenceComplete(JpSentenceBean jpSentenceBean);
    }

    interface TextbookDetailsPresenter extends IBasePresenter<TextbookDetailsView> {

        /**
         * 获取
         *
         * @param source
         * @param sourceid
         * @param flag
         */
        void getJapanesePdfFile(String source, String sourceid, int flag);

        void getJapanesePdfFileWithCn(String source, String sourceid, int flag);

        void updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile);


        void getJpSentence(String name, String sourceid);
    }

    interface TextbookDetailsModel extends BaseModel {

        Disposable getJapanesePdfFile(String source, String sourceid, PdfCallback callback);

        Disposable getJapanesePdfFileWithCn(String source, String sourceid, PdfCallback callback);

        Disposable updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile, UpdateScoreCallback updateScoreCallback);

        Disposable getJpSentence(String name, String sourceid, JpSentenceCallback jpSentenceCallback);
    }


    interface JpSentenceCallback {

        void success(JpSentenceBean jpSentenceBean);

        void error(Exception e);
    }

    interface UpdateScoreCallback {

        void success(UpdateScoreBean updateScoreBean);

        void error(Exception e);
    }

    interface PdfCallback {

        void success(PdfFileBean pdfFileBean);

        void error(Exception e);
    }
}
