package com.iyuba.biaori.simplified.presenter.me;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.model.bean.SyncListenBean;
import com.iyuba.biaori.simplified.model.bean.login.LogoffBean;
import com.iyuba.biaori.simplified.model.bean.me.GroupBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncEvalBean;
import com.iyuba.biaori.simplified.model.bean.me.SyncWordBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.model.me.MeModel;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.view.me.MeContract;
import com.iyuba.biaori.simplified.view.vip.VipContract;
import com.iyuba.headlinelibrary.data.model.Word;
import com.iyuba.module.toolbox.MD5;

import org.litepal.LitePal;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MePresenter extends BasePresenter<MeContract.MeView, MeContract.MeModel>
        implements MeContract.MePresenter {

    private int page = 1;

    private List<String> bookName;

    private int position = 0;

    @Override
    protected MeContract.MeModel initModel() {
        return new MeModel();
    }

    @Override
    public void logoff(int protocol, String username, String password, String format, String sign) {

        Disposable disposable = model.logoff(protocol, username, password, format, sign, new MeContract.LogoffCallback() {
            @Override
            public void success(LogoffBean logoffBean) {

                view.hideProgressDialog();
                if (logoffBean.getResult().equals("101")) {

                    view.logoffComplete(logoffBean);
                    view.toast("注销成功");
                } else if (logoffBean.getResult().equals("103")) {

                    view.toast("密码输入错误");
                }
            }

            @Override
            public void error(Exception e) {

                view.hideProgressDialog();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage, int TestMode, String sign, String Lesson) {

        view.showLoading("正在同步数据...");
        Disposable disposable = model.getStudyRecordByTestMode(format, uid, Pageth, NumPerPage, TestMode, sign, Lesson, new MeContract.ListenCallback() {

            @SuppressLint("Range")
            @Override
            public void success(SyncListenBean syncListenBean) {

                if (syncListenBean.getResult().equals("1")) {

                    List<SyncListenBean.DataDTO> dataDTOList = syncListenBean.getData();
                    for (int i = 0; i < dataDTOList.size(); i++) {

                        SyncListenBean.DataDTO dataDTO = dataDTOList.get(i);
                        List<Book> bookList = LitePal.where("name = ?", dataDTO.getLesson().toLowerCase()).find(Book.class);
                        for (int j = 0; j < bookList.size(); j++) {//有两本书，但不知道是哪一本

                            Book book = bookList.get(j);
                            List<JpLesson> jpLessonList = LitePal.where(" source = ? and lessonid = ?", book.getSource() + "", dataDTO.getLessonId()).find(JpLesson.class);
                            if (jpLessonList.size() > 0) {//有数据

                                JpLesson jl = jpLessonList.get(0);
                                if (jl.getTestNumber() == 0) {//进度为0则更新

                                    jl.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                    jl.updateAll(" source = ? and lessonid = ?", book.getSource() + "", dataDTO.getLessonId());
                                } else {//有进度

                                    if (jl.getEndTime() == null) {//进度记录时间为null

                                        jl.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                        jl.updateAll(" source = ? and lessonid = ?", book.getSource() + "", dataDTO.getLessonId());
                                    } else {//有进度时间

                                        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                                        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                                        try {
                                            long saveTime = simpleDateFormat.parse(jl.getEndTime()).getTime();
                                            long dataTime = simpleDateFormat.parse(dataDTO.getEndTime()).getTime();
                                            if (saveTime < dataTime) {//存储的时间小于数据的时间

                                                jl.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                                jl.setEndTime(dataDTO.getEndTime());
                                                jl.updateAll(" source = ? and lessonid = ?", book.getSource() + "", dataDTO.getLessonId());
                                            }
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            } //
                        }
                    }

                    if (syncListenBean.getData().size() == NumPerPage) {

                        page++;
                        handler.sendEmptyMessageDelayed(1, 400);
                    } else {//同步完成

                        Cursor cursor = LitePal.findBySQL("select  DISTINCT  name from book");
                        bookName = new ArrayList<>();
                        while (cursor.moveToNext()) {

                            bookName.add(cursor.getString(cursor.getColumnIndex("name")));
                        }
                        cursor.close();
                        position = 0;
                        getTestRecord(Constant.userinfo.getUid() + "", bookName.get(position));
                    }
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

    @Override
    public void getTestRecord(String userId, String newstype) {

        Disposable disposable = model.getTestRecord(userId, newstype, new MeContract.EvalCallback() {
            @Override
            public void success(SyncEvalBean syncEvalBean) {

                if (syncEvalBean.getResult().equals("1")) {


                    DecimalFormat decimalFormat = new DecimalFormat("#000");
                    DecimalFormat dF00 = new DecimalFormat("#00");

                    List<SyncEvalBean.DataDTO> dataDTOList = syncEvalBean.getData();
                    for (int i = 0; i < dataDTOList.size(); i++) {

                        SyncEvalBean.DataDTO dataDTO = dataDTOList.get(i);
                        List<Sentence> sentenceList = LitePal
                                .where("sourceid = ? and source = ? and idindex = ?", decimalFormat.format(dataDTO.getNewsid()), dataDTO.getNewstype(), dataDTO.getIdindex() + "")
                                .find(Sentence.class);
                        if (sentenceList.size() > 0) {//有数据更新

                            Sentence sentence = sentenceList.get(0);
                            sentence.setScore(dataDTO.getScore());
                            sentence.setRecordSoundUrl(dataDTO.getUrl());
                            sentence.updateAll("sourceid = ? and source = ? and idindex = ?", decimalFormat.format(dataDTO.getNewsid()), dataDTO.getNewstype(), dataDTO.getIdindex() + "");
                        } else {//没数据则添加

                            Sentence sentence = new Sentence();
                            sentence.setSourceid(decimalFormat.format(dataDTO.getNewsid()));
                            sentence.setSource(dataDTO.getNewstype());
                            sentence.setSentence(dataDTO.getSentence());
                            sentence.setScore(dataDTO.getScore());
                            sentence.setIdindex(dF00.format(dataDTO.getIdindex()));
                            sentence.setRecordSoundUrl(dataDTO.getUrl());
                            sentence.save();
                        }
                    }
                }


                position++;
                //同步完成一个课本，再同步下一个
                if (position < bookName.size()) {

                    getTestRecord(Constant.userinfo.getUid() + "", bookName.get(position));
                } else {

                    position = 0;
                    String signStr = Constant.userinfo.getUid() + bookName.get(position) + 2 + "W" + Constant.APPID + DateUtil.getCurDate();
                    String sign = MD5Util.MD5(signStr);
                    getExamDetail(Constant.APPID, Constant.userinfo.getUid() + "", bookName.get(position), "W",
                            2, sign, "json");
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

    @Override
    public void getExamDetail(int appId, String uid, String lesson, String TestMode, int mode, String sign, String format) {

        Disposable disposable = model.getExamDetail(appId, uid, lesson, TestMode, mode, sign, format, new MeContract.WordCallback() {
            @Override
            public void success(SyncWordBean syncWordBean) {

                if (syncWordBean.getResult() == 1) {

                    List<SyncWordBean.WordDataDTO> wrongDataDTOList = syncWordBean.getDataWrong();
                    for (int i = 0; wrongDataDTOList != null && i < wrongDataDTOList.size(); i++) {

                        SyncWordBean.WordDataDTO wordDataDTO = wrongDataDTOList.get(i);
                        List<JpWord> wordList = LitePal.where("wordid = ?", wordDataDTO.getId() + "").find(JpWord.class);
                        if (wordList.size() > 0) {//更新

                            JpWord word = wordList.get(0);
                            word.setAnswer_status(2);
                            word.updateAll("wordid = ?", wordDataDTO.getId() + "");
                        } else {//添加数据

                            JpWord jpWord = new JpWord();
                            jpWord.setAnswer_status(2);
                            jpWord.setId(wordDataDTO.getId());
                            jpWord.save();
                        }
                    }
                    //正确
                    List<SyncWordBean.WordDataDTO> rightDataDTOList = syncWordBean.getDataRight();
                    for (int i = 0; rightDataDTOList != null && i < rightDataDTOList.size(); i++) {

                        SyncWordBean.WordDataDTO wordDataDTO = rightDataDTOList.get(i);
                        List<JpWord> wordList = LitePal.where("wordid = ?", wordDataDTO.getId() + "").find(JpWord.class);
                        if (wordList.size() > 0) {//更新

                            JpWord word = wordList.get(0);
                            word.setAnswer_status(1);
                            word.updateAll("wordid = ?", wordDataDTO.getId() + "");
                        } else {//添加数据

                            JpWord jpWord = new JpWord();
                            jpWord.setAnswer_status(1);
                            jpWord.setId(wordDataDTO.getId());
                            jpWord.save();
                        }
                    }
                }

                //同步数据
                position++;
                if (position < bookName.size()) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String signStr = Constant.userinfo.getUid() + bookName.get(position) + 2 + "W" + Constant.APPID + DateUtil.getCurDate();
                            String sign = MD5Util.MD5(signStr);
                            getExamDetail(Constant.APPID, Constant.userinfo.getUid() + "", bookName.get(position), "W",
                                    2, sign, "json");

                        }
                    }, 500);

                } else {

                    view.hideLoading();
                    view.toast("同步数据完成");
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

    @Override
    public void getQQGroup(String type) {

        Disposable disposable = model.getQQGroup(type, new VipContract.JpQQ2Callback() {
            @Override
            public void success(JpQQBean2 jpQQBean2) {

                if (jpQQBean2.getMessage().equals("true")) {

                    //获取客服qq
                    getJpQQ(Constant.APPID, jpQQBean2);
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
    public void getJpQQ(int appid, JpQQBean2 jpQQBean2) {

        Disposable disposable = model.getJpQQ(appid, new VipContract.JpQQCallback() {
            @Override
            public void success(JpQQBean jpQQBean) {

                if (jpQQBean.getResult() == 200) {

                    view.showQQDialog(jpQQBean, jpQQBean2);
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
    public void entergroup(String uid, String apptype) {

        Disposable disposable = model.entergroup(uid, apptype, new MeContract.Callback() {
            @Override
            public void success(GroupBean groupBean) {


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


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            String sign = MD5.getMD5ofStr(Constant.userinfo.getUid() + DateUtil.getCurDate());
            getStudyRecordByTestMode("json", Constant.userinfo.getUid(), page, 10, 1, sign, "biaori");
            return false;
        }
    });
}
