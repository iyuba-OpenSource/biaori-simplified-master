package com.iyuba.biaori.simplified.presenter.break_through;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iyuba.biaori.simplified.entity.ExamRecordPost;
import com.iyuba.biaori.simplified.model.bean.break_through.ExamRecordBean;
import com.iyuba.biaori.simplified.model.break_through.WordAnswerModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.break_through.WordAnswerContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class WordAnswerPresenter extends BasePresenter<WordAnswerContract.WordAnswerView, WordAnswerContract.WordAnswerModel>
        implements WordAnswerContract.WordAnswerPresenter {


    @Override
    protected WordAnswerContract.WordAnswerModel initModel() {
        return new WordAnswerModel();
    }

    @Override
    public void updateExamRecord(ExamRecordPost bean) {

        Disposable disposable = model.updateExamRecord(bean, new WordAnswerContract.Callback() {
            @Override
            public void success(ResponseBody examRecordBean) {

                String resultStr = null;
                try {
                    resultStr = examRecordBean.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resultStr = resultStr.trim();
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);

                    if (jsonObject.getString("result").equals("1")) {

                        if (jsonObject.getInt("jiFen") == 0) {

                            view.toast("测评数据成功同步到云端。");
                        } else {

                            view.toast("测评数据成功同步到云端 +" + jsonObject.getInt("jiFen") + "积分");
                        }
                    } else {

                        view.toast("测评数据同步到云端失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
//        addSubscribe(disposable);
    }
}
