package com.iyuba.biaori.simplified.ui.me;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.TextbookAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityMyCollectBinding;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.NewCollectBean;
import com.iyuba.biaori.simplified.presenter.me.MyCollectPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.textbook.TextbookDetailsActivity;
import com.iyuba.biaori.simplified.util.popup.LoadingPopup;
import com.iyuba.biaori.simplified.view.me.MyCollectContract;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 */
public class LessonCollectActivity extends BaseActivity<MyCollectContract.MyCollectView, MyCollectContract.MyCollectPresenter>
        implements MyCollectContract.MyCollectView {

    private ActivityMyCollectBinding activityMyCollectBinding;

    private TextbookAdapter textbookAdapter;
    private DecimalFormat decimalFormat;

    private int pageNum = 1;

    private LoadingPopup loadingPopup;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;


        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decimalFormat = new DecimalFormat("#000");
        initOperation();
    }

    /**
     * 初始化数据
     */
    private void initOperation() {

        activityMyCollectBinding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        activityMyCollectBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.icon_sycn);
        activityMyCollectBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                synData();
            }
        });
        activityMyCollectBinding.toolbar.toolbarIvTitle.setText("课文收藏");
        activityMyCollectBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        List<JpLesson> jpLessonList = getCollect();

        textbookAdapter = new TextbookAdapter(R.layout.item_textbook, jpLessonList);
        activityMyCollectBinding.mycollectRv.setLayoutManager(new LinearLayoutManager(this));
        activityMyCollectBinding.mycollectRv.setAdapter(textbookAdapter);
        textbookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TextbookDetailsActivity.startActivity(LessonCollectActivity.this, position, textbookAdapter.getData());
            }
        });

        if (jpLessonList.size() == 0) {

            synData();
        }
    }

    /**
     * 启动同步数据
     */
    private void synData() {


        showLoading("正在同步数据");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pageNum = 1;
                presenter.getCollect_wx(Constant.userinfo.getUid() + "", "Iyuba", "japan"
                        , 0, pageNum, 10);
            }
        }, 800);
    }

    /**
     * 获取收藏的课文
     *
     * @return
     */
    private List<JpLesson> getCollect() {

        List<JpLesson> jpLessonList = LitePal.where("collect = 1").find(JpLesson.class);
        for (int i = 0; i < jpLessonList.size(); ) {

            JpLesson jpLesson = jpLessonList.get(i);
            if (jpLesson.getLesson() == null || jpLesson.getLesson().equals("")) {

                jpLessonList.remove(jpLesson);
            } else {

                i++;
            }
        }
        return jpLessonList;
    }

    @Override
    protected void onResume() {
        super.onResume();

        textbookAdapter.setNewData(getCollect());
    }

    @Override
    public View initLayout() {
        activityMyCollectBinding = ActivityMyCollectBinding.inflate(getLayoutInflater());
        return activityMyCollectBinding.getRoot();
    }

    @Override
    public MyCollectContract.MyCollectPresenter initPresenter() {
        return new MyCollectPresenter();
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(this);
        }
        loadingPopup.setContent(msg);
        loadingPopup.setOutSideDismiss(false);
        loadingPopup.setBackPressEnable(false);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getCollectComplete(NewCollectBean newCollectBean) {


        List<NewCollectBean.DataDTO> dtoList = newCollectBean.getData();

        for (int i = 0; i < dtoList.size(); i++) {

            NewCollectBean.DataDTO dataDTO = dtoList.get(i);

            //是否有此数据，没有则插入数据库，有则更新
            List<JpLesson> jpLessons = LitePal.where("lessonID = ? and source = ?", dataDTO.getVoaid(), dataDTO.getSource()).find(JpLesson.class);
            if (jpLessons.size() == 0) {

                JpLesson jpLesson = new JpLesson();
                jpLesson.setLessonID(dataDTO.getVoaid());
                jpLesson.setCollect(1);
                jpLesson.setLessonCH(dataDTO.getContentcn());
                jpLesson.setLesson(dataDTO.getContent());
                jpLesson.setSound(dataDTO.getSound());
                jpLesson.setSource(dataDTO.getSource());
                jpLesson.save();
            } else {

                JpLesson lesson = jpLessons.get(0);
                lesson.setCollect(1);
                lesson.updateAll("lessonID = ?  and source = ?", dataDTO.getVoaid(), dataDTO.getSource());
            }
        }

        if (dtoList.size() == 10) {

            pageNum++;
            presenter.getCollect_wx(Constant.userinfo.getUid() + "", "Iyuba", "japan"
                    , 0, pageNum, 10);
        } else {

            hideLoading();
            toast("同步数据完成");
            textbookAdapter.setNewData(getCollect());
        }
    }
}