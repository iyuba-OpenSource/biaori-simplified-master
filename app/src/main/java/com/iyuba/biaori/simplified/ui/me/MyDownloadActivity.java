package com.iyuba.biaori.simplified.ui.me;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.me.MyDownloadAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityMyDownloadBinding;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.ui.textbook.TextbookDetailsActivity;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的下载
 */
public class MyDownloadActivity extends AppCompatActivity {

    private ActivityMyDownloadBinding activityMyDownloadBinding;

    private MyDownloadAdapter myDownloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyDownloadBinding = ActivityMyDownloadBinding.inflate(getLayoutInflater());
        setContentView(activityMyDownloadBinding.getRoot());

        activityMyDownloadBinding.toolbar.toolbarIvTitle.setText("音频下载");
        activityMyDownloadBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        activityMyDownloadBinding.downloadRv.setLayoutManager(new LinearLayoutManager(this));
        myDownloadAdapter = new MyDownloadAdapter(R.layout.item_textbook_download, new ArrayList<>());
        activityMyDownloadBinding.downloadRv.setAdapter(myDownloadAdapter);
        myDownloadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TextbookDetailsActivity.startActivity(MyDownloadActivity.this, position, myDownloadAdapter.getData());
            }
        });
        myDownloadAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.td_tv_del) {

                    JpLesson jpLesson = myDownloadAdapter.getItem(position);
                    delAlert(jpLesson);
                }
            }
        });


        List<JpLesson> jpLessonList = LitePal.where("downloadFlag = 1").find(JpLesson.class);

        String mp3Dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        for (int i = 0; i < jpLessonList.size(); ) {

            JpLesson jpLesson = jpLessonList.get(i);
            String path = mp3Dir + jpLesson.getSound().replace("http://static2.iyuba.cn", "");
            if (!new File(path).exists()) {

                //不存在文件则更新数据库的
                List<JpLesson> lessonList = LitePal
                        .where("source = ? and lessonid = ?", jpLesson.getSource(), jpLesson.getLessonID())
                        .find(JpLesson.class);
                if (lessonList.size() > 0) {

                    JpLesson jp = lessonList.get(0);
                    jp.setDownloadFlag(0);
                    jp.updateAll("source = ? and lessonid =?", jpLesson.getSource(), jpLesson.getLessonID());
                }
                //在列表中删除
                jpLessonList.remove(i);
            } else {
                i++;
            }
        }
        myDownloadAdapter.setNewData(jpLessonList);
    }


    private void delAlert(JpLesson jpLesson) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否删除?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mp3Path = getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() +
                                jpLesson.getSound().replace("http://static2.iyuba.cn", "");
                        File audioFile = new File(mp3Path);
                        if (audioFile.exists()) {

                            audioFile.delete();
                            List<JpLesson> litePalList = LitePal
                                    .where("lessonid = ? and  source = ?", jpLesson.getLessonID(), jpLesson.getSource())
                                    .find(JpLesson.class);
                            if (litePalList.size() > 0) {

                                JpLesson jpLesson1 = litePalList.get(0);
                                jpLesson1.setDownloadFlag(0);
                                jpLesson1.updateAll("lessonid = ? and  source = ?", jpLesson1.getLessonID(), jpLesson1.getSource());
                                myDownloadAdapter.getData().remove(jpLesson);
                                myDownloadAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }
}