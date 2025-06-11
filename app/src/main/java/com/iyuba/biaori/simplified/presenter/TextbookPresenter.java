package com.iyuba.biaori.simplified.presenter;

import android.util.Log;

import com.iyuba.biaori.simplified.model.TextbookModel;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.JpLessonBean;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.view.TextbookContract;
import com.iyuba.biaori.simplified.view.WelcomeContract;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class TextbookPresenter extends BasePresenter<TextbookContract.TextbookView, TextbookContract.TextbookModel>
        implements TextbookContract.TextbookPresenter {


    @Override
    protected TextbookContract.TextbookModel initModel() {
        return new TextbookModel();
    }

    @Override
    public void getJpLesson(String source) {

        Disposable disposable = model.getJpLesson(source, new TextbookContract.JpLessonCallback() {
            @Override
            public void success(JpLessonBean jpLessonBean) {

                if (jpLessonBean.getResult() == 200) {

                    view.getJpLessonComplete(jpLessonBean);
                } else {

                    view.getJpLessonComplete(jpLessonBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.getJpLessonComplete(null);
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getCollect(int userId, String sign, String topic, int appid, int sentenceFlg) {

        Disposable disposable = model.getCollect(userId, sign, topic, appid, sentenceFlg, new TextbookContract.CollectCallback() {
            @Override
            public void success(CollectBean collectBean) {

                if (collectBean.getResult() == 1) {

                    view.getCollectComplete(collectBean);
                }
            }

            @Override
            public void error(Exception e) {

                Log.d("textbook", e.toString());
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new TextbookContract.ADCallback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeans) {

                if (adEntryBeans.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeans.get(0);
                    if (adEntryBean.getResult().equals("1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }
}
