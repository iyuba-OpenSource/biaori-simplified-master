package com.iyuba.biaori.simplified.view.textbook;


import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.BaseModel;
import com.iyuba.biaori.simplified.presenter.IBasePresenter;
import com.iyuba.biaori.simplified.view.BaseView;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public interface MediaContract {

    interface MediaView extends BaseView {

        /**
         * 村塾数据到数据库完成
         */
//        void saveNewContentComplete(int logPosition);
    }

    interface MediaPresenter extends IBasePresenter<MediaView> {

        /**
         * @param format
         * @param uid
         * @param BeginTime
         * @param EndTime
         * @param Lesson
         * @param TestMode
         * @param TestWords
         * @param platform
         * @param appName
         * @param DeviceId
         * @param LessonId
         * @param sign
         * @param EndFlg
         * @param TestNumber
         * @param book       传递book的source
         */
        void updateStudyRecordNew(String format, String uid,
                                  String BeginTime, String EndTime,
                                  String Lesson, String TestMode,
                                  String TestWords, String platform,
                                  String appName, String DeviceId,
                                  String LessonId, String sign, int EndFlg, int TestNumber, Book book);

    }

    interface MediaModel extends BaseModel {

        Disposable updateStudyRecordNew(String format, String uid,
                                        String BeginTime, String EndTime,
                                        String Lesson, String TestMode,
                                        String TestWords, String platform,
                                        String appName, String DeviceId,
                                        String LessonId, String sign, int EndFlg, int TestNumber, Callback callback);

    }


    interface Callback {

        void success(ResponseBody responseBody);

        void error(Exception e);

    }

}
