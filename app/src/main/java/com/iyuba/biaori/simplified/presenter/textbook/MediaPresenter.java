package com.iyuba.biaori.simplified.presenter.textbook;

import android.widget.Toast;

import com.google.gson.Gson;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.entity.RewardEventbus;
import com.iyuba.biaori.simplified.model.bean.textbook.TestRecordBean;
import com.iyuba.biaori.simplified.model.textbook.MediaModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.view.textbook.MediaContract;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class MediaPresenter extends BasePresenter<MediaContract.MediaView, MediaContract.MediaModel>
        implements MediaContract.MediaPresenter {


    @Override
    protected MediaContract.MediaModel initModel() {
        return new MediaModel();
    }


    @Override
    public void updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime, String Lesson,
                                     String TestMode, String TestWords, String platform, String appName, String DeviceId,
                                     String LessonId, String sign, int EndFlg, int TestNumber, Book book) {


        model.updateStudyRecordNew(format, uid, BeginTime, EndTime, Lesson, TestMode, TestWords, platform,
                appName, DeviceId, LessonId, sign, EndFlg, TestNumber, new MediaContract.Callback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        TestRecordBean testRecordBean = null;
                        try {
                            testRecordBean = new Gson().fromJson(responseBody.string().trim(), TestRecordBean.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (testRecordBean != null && testRecordBean.getResult().equals("1")) {

                            if (!testRecordBean.getReward().equals("0")) {

                                EventBus.getDefault().post(new RewardEventbus(Integer.parseInt(testRecordBean.getReward())));
                            }

                            //todo  保存播放进度到数据库
//                            headlinesDataManager.updateListenProgress(Integer.parseInt(LessonId), EndFlg, TestNumber);
                            List<JpLesson> jpLessonList = LitePal.where("lessonid = ? and source = ?", LessonId, book.getSource() + "").find(JpLesson.class);
                            if (jpLessonList.size() > 0) {

                                JpLesson jpLesson = jpLessonList.get(0);
                                jpLesson.setTestNumber(TestNumber);
                                jpLesson.updateAll("lessonid = ? and source = ?", LessonId, book.getSource() + "");
                            }
                        }
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
    }
}
