package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PdfFileBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.model.textbook.TextbookDetailsModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.OriginalContract;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class TextbookDetailsPresenter extends BasePresenter<TextbookDetailsContract.TextbookDetailsView, TextbookDetailsContract.TextbookDetailsModel>
        implements TextbookDetailsContract.TextbookDetailsPresenter {

    @Override
    protected TextbookDetailsContract.TextbookDetailsModel initModel() {
        return new TextbookDetailsModel();
    }

    @Override
    public void getJapanesePdfFile(String source, String sourceid, int flag) {

        Disposable disposable = model.getJapanesePdfFile(source, sourceid, new TextbookDetailsContract.PdfCallback() {
            @Override
            public void success(PdfFileBean pdfFileBean) {

                if (pdfFileBean.getResult().equals("1")) {

                    view.showPdfDialog(pdfFileBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getJapanesePdfFileWithCn(String source, String sourceid, int flag) {

        Disposable disposable = model.getJapanesePdfFileWithCn(source, sourceid, new TextbookDetailsContract.PdfCallback() {
            @Override
            public void success(PdfFileBean pdfFileBean) {

                if (pdfFileBean.getResult().equals("1")) {

                    view.showPdfDialog(pdfFileBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile) {

        Disposable disposable = model.updateScore(flag, uid, appid, idindex, srid, mobile, new TextbookDetailsContract.UpdateScoreCallback() {
            @Override
            public void success(UpdateScoreBean updateScoreBean) {

                if (updateScoreBean.getResult().equals("200")) {

                    view.updateScore(updateScoreBean,srid);
                    view.toast("扣除积分成功");
                } else {

                    view.toast(updateScoreBean.getMessage());
                }

            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getJpSentence(String name, String sourceid) {

        Disposable disposable = model.getJpSentence(name, sourceid, new TextbookDetailsContract.JpSentenceCallback() {
            @Override
            public void success(JpSentenceBean jpSentenceBean) {

                if (jpSentenceBean.getResult() == 200) {

                    view.getJpSentenceComplete(jpSentenceBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.getJpSentenceComplete(null);
            }
        });
        addSubscribe(disposable);
    }

}
