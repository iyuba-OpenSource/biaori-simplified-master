package com.iyuba.biaori.simplified.presenter.dubbing;

import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.dubbing.DubbingModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.dubbing.DubbingContract;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DubbingPresenter extends BasePresenter<DubbingContract.DubbingView, DubbingContract.DubbingModel>
        implements DubbingContract.DubbingPresenter {


    @Override
    protected DubbingContract.DubbingModel initModel() {
        return new DubbingModel();
    }

    @Override
    public void textExamApi(String format, String voaid) {

        Disposable disposable = model.textExamApi(format, voaid, new DubbingContract.Callback() {
            @Override
            public void success(DubbingTextBean dubbingTextBean) {

                view.textExamApiComplete(dubbingTextBean);
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void jtest(int userId, int IdIndex, int paraId, int newsId, String type, String filePath, String sentence) {

        Map<String, RequestBody> params = getMap(userId, IdIndex, paraId, newsId, type, filePath, sentence);
        Disposable disposable = model.jtest(params, new DubbingContract.TestCallback() {
            @Override
            public void success(EvaluteRecordBean evaluteRecordBean) {

                if (evaluteRecordBean.getResult().equals("1")) {

                    view.jtestComplete(evaluteRecordBean, paraId);
                }
            }

            @Override
            public void error(Exception e) {

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
