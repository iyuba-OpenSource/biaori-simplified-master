package com.iyuba.biaori.simplified.ui.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.entity.ShareInfoRecord;
import com.iyuba.biaori.simplified.model.bean.me.ShareInfoBean;
import com.iyuba.biaori.simplified.presenter.me.CalendarPresenter;
import com.iyuba.biaori.simplified.view.me.CalendarContract;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * 打卡日历
 */
public class CalendarActivity extends AppCompatActivity implements CalendarContract.CalendarView {
    private static final String TAG = CalendarActivity.class.getSimpleName();

    private Context mContext;
    TextView mTimeTitle;
    TextView chooseDate;
    CalendarView calendarView;
    private ProgressDialog mLoadingDialog;
    private int[] cDate = CalendarUtil.getCurrentDate();
    private String curTime = "";
    private int flag = 0;

    public TextView mTxtRight;
    public CheckBox mImgRight;
    public TextView mTitle;
    public ImageView img_right;

    AppBarLayout top;

    Toolbar toolbar;

    private CalendarPresenter calendarPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mContext = this;

        calendarPresenter = new CalendarPresenter();
        calendarPresenter.attchView(this);


        initView();
        mTitle.setText("打卡报告");
        setBackListener();
    }

    private void initView() {

        top = findViewById(R.id.top);
        mTxtRight = top.findViewById(R.id.txt_right);
        mImgRight = top.findViewById(R.id.iv_right);
        mTitle = top.findViewById(R.id.title);
        img_right = top.findViewById(R.id.img_right);
        toolbar = top.findViewById(R.id.toolbar);
    }

    private void setBackListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        chooseDate = findViewById(R.id.choose_date);
        calendarView = findViewById(R.id.calendar);
        mTimeTitle = findViewById(R.id.time_title);

        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView
                .setStartEndDate("2016.1", "2028.12")
                .setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();
        mTimeTitle.setText(cDate[0] + "年" + cDate[1] + "月");
        chooseDate.setText("今天的日期：" + cDate[0] + "年" + cDate[1] + "月" + cDate[2] + "日");

        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                if (date == null) {
                    Log.e(TAG, "onPagerChanged data is null? ");
                    return;
                }
                mTimeTitle.setText(date[0] + "年" + date[1] + "月");
                Log.e(TAG, "onPagerChanged flag " + flag);
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                mTimeTitle.setText(date.getSolar()[0] + "年" + date.getSolar()[1] + "月");
                if (date.getType() == 1) {
                    chooseDate.setText("今天的日期：" + date.getSolar()[0] + "年" + date.getSolar()[1] + "月" + date.getSolar()[2] + "日");
                }
            }
        });
//        if (NetworkUtils.isNetworkAvailable()) {
        getCalendar(cDate);
//        } else {
//            showToast(R.string.networkError);
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (calendarPresenter != null) {

            calendarPresenter.detachView();
        }
    }

    private void getCalendar(int[] curDate) {

        if (curDate[1] < 10) {
            curTime = curDate[0] + "0" + curDate[1];
        } else {
            curTime = curDate[0] + "" + curDate[1];
        }
        Log.e(TAG, "getCalendar curTime " + curTime);

        calendarPresenter.getShareInfoShow(Constant.userinfo.getUid() + "", Constant.APPID + "", curTime);
    }

    public void showRankings(List<ShareInfoRecord> ranking) {

        HashMap<String, String> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        for (ShareInfoRecord record : ranking) {
            Log.e(TAG, "showCalendar record.createtime: " + record.createtime);
            try {
                Date date = sdf.parse(record.createtime);
//                Log.e(TAG, "showCalendar date: " + date);
//                Log.e(TAG, "showCalendar getYear: " + date.getYear());
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.setTime(date);

                String item = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
                Log.e(TAG, "showCalendar item: " + item);
                if (record.scan > 1) {
                    map.put(item, "" + record.scan);
                } else {
                    map.put(item, "true");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendarView
                .setStartEndDate("2016.1", "2028.12")
                .setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

        calendarView.setClockInStatus(map);
    }

    public void lastMonth(View view) {

        flag = -1;
        showLoadingLayout();
        if (cDate[1] > 0) {
            cDate[1]--;
        } else {
            cDate[0]--;
            cDate[1] = 12;
        }
        getCalendar(cDate);

        calendarView.lastMonth();
    }

    public void nextMonth(View view) {

        flag = 1;
        showLoadingLayout();
        if (cDate[1] < 12) {
            cDate[1]++;
        } else {
            cDate[0]++;
            cDate[1] = 1;
        }
        getCalendar(cDate);

        calendarView.nextMonth();
    }

    public void showLoadingLayout() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(mContext);
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingLayout() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    }

    public void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String msg) {

        showLoadingLayout();
    }

    @Override
    public void hideLoading() {

        dismissLoadingLayout();
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getShareInfoShowComplete(ShareInfoBean shareInfoBean) {


        dismissLoadingLayout();
        List<ShareInfoRecord> rankingList = shareInfoBean.getRecord();
        if (rankingList == null || rankingList.size() < 1) {
            Log.e(TAG, "getRanking onNext empty.");
            runOnUiThread(() -> showRankings(new ArrayList<>()));
        } else {
            runOnUiThread(() -> showRankings(rankingList));
        }

    }
}
