package com.iyuba.biaori.simplified.presenter.me;

import com.iyuba.biaori.simplified.model.bean.me.WordPdfBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordCollectBean;
import com.iyuba.biaori.simplified.model.me.WordCollectModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.me.WordCollectContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class WordCollectPresenter extends BasePresenter<WordCollectContract.WordCollectView, WordCollectContract.WordCollectModel>
        implements WordCollectContract.WordCollectPresenter {

    @Override
    protected WordCollectContract.WordCollectModel initModel() {
        return new WordCollectModel();
    }

    @Override
    public void getWordToPDF(int userid, String type, String ntype) {

        Disposable disposable = model.getWordToPDF(userid, type, ntype, new WordCollectContract.WordPdfCallback() {
            @Override
            public void success(WordPdfBean wordPdfBean) {

                view.getWordToPDF(wordPdfBean);
            }

            @Override
            public void error(Exception e) {

                view.toast("请求超时");
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getWordCollect(String userid, String type, int appid) {

        Disposable disposable = model.getWordCollect(userid, type, appid, new WordCollectContract.Callback() {
            @Override
            public void success(JpWordCollectBean jpWordCollectBean) {

                view.hideLoading();
                if (jpWordCollectBean.getResult().equals("1")) {

                    view.getWordCollected(jpWordCollectBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
