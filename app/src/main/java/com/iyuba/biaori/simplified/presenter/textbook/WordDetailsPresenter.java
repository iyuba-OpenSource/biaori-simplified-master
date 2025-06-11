package com.iyuba.biaori.simplified.presenter.textbook;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.model.bean.WordCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.textbook.WordDetailsModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.WordDetailsContract;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.PUT;

public class WordDetailsPresenter extends BasePresenter<WordDetailsContract.WordDetailsView, WordDetailsContract.WordDetailsModel>
        implements WordDetailsContract.WordDetailsPresenter {

    @Override
    protected WordDetailsContract.WordDetailsModel initModel() {
        return new WordDetailsModel();
    }

    @Override
    public void jtest(int userId, int IdIndex, int paraId, int newsId, String type, String filePath, String sentence) {

        Map<String, RequestBody> params = getMap(userId, IdIndex, paraId, newsId, type, filePath, sentence);
        Disposable disposable = model.jtest(params, new WordDetailsContract.EvaluteRecordCallback() {
            @Override
            public void success(EvaluteRecordBean evaluteRecordBean) {

                if (evaluteRecordBean.getResult().equals("1")) {

                    view.jtestComplete(evaluteRecordBean);
                } else {

                    view.toast(evaluteRecordBean.getMessage());
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
    public void updateWordCollect(String userid, String wordid, String type, String appid) {

        Disposable disposable = model.updateWordCollect(userid, wordid, type, appid, new WordDetailsContract.WordCollectCallback() {
            @Override
            public void success(WordCollectBean wordCollectBean) {

                if (wordCollectBean.getResult().equals("1")) {

                    view.updateCollectComplete(wordid, type);
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

    private Map<String, RequestBody> getMap(int userId, int IdIndex, int paraId, int newsId, String type, String filePath, String sentence) {


        File file = new File(filePath);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("userId", toRequestBody(userId + ""));
        params.put("IdIndex", toRequestBody(IdIndex + ""));
        params.put("paraId", toRequestBody(paraId + ""));
        params.put("newsId", toRequestBody(newsId + ""));
        params.put("type", toRequestBody(type));
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        params.put("file\";filename=\"" + file.getName(), requestFile);
        String urlSentence = null;
        try {
            urlSentence = URLEncoder.encode(sentence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlSentence = urlSentence.replaceAll("\\+", "%20");
        params.put("sentence", toRequestBody(urlSentence));

        return params;
    }

    private RequestBody toRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }
}
