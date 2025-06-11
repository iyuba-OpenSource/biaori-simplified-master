package com.iyuba.biaori.simplified.ui.vip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.vip.VipAdapter;
import com.iyuba.biaori.simplified.adapter.vip.VipKindAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityMyCollectBinding;
import com.iyuba.biaori.simplified.databinding.ActivityVipBinding;
import com.iyuba.biaori.simplified.entity.LoginEventBus;
import com.iyuba.biaori.simplified.entity.VipKind;
import com.iyuba.biaori.simplified.model.bean.MoreInfoBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.presenter.vip.VipPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.util.QQUtil;
import com.iyuba.biaori.simplified.util.TimeUtils;
import com.iyuba.biaori.simplified.view.vip.VipContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 会员中心
 */
public class VipActivity extends BaseActivity<VipContract.VipView, VipContract.VipPresenter>
        implements View.OnClickListener, VipContract.VipView {

    private ActivityVipBinding activityVipBinding;
    /**
     * 会员类型
     */
    private VipAdapter vipAdapter;

    /**
     * 会员价格
     */
    private VipKindAdapter vipKindAdapter;


    /**
     * 0:本应用会员
     * 10：黄金会员
     */
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getResources().getColor(R.color.shallow_orange));
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        getBundle();
        initView();
        bindView();

        if (Constant.userinfo != null) {

            String signStr = "20001" + Constant.userinfo.getUid() + "iyubaV2";
            String sign = MD5Util.MD5(signStr);
            presenter.getMoreInfo("android", 20001, Constant.userinfo.getUid(), Constant.userinfo.getUid(), Constant.APPID, sign);
        }
    }

    @Override
    public View initLayout() {

        activityVipBinding = ActivityVipBinding.inflate(getLayoutInflater());
        return activityVipBinding.getRoot();
    }

    @Override
    public VipContract.VipPresenter initPresenter() {
        return new VipPresenter();
    }


    /**
     * 登录后触发刷新
     *
     * @param loginEventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginEventBus(LoginEventBus loginEventBus) {

        updateUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            flag = bundle.getInt("FLAG");
        } else {

            flag = 0;
        }
    }

    /**
     * @param activity
     * @param flag     0 本引用会员 10黄金会员
     */
    public static void startActivity(Activity activity, int flag) {

        Intent intent = new Intent(activity, VipActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("FLAG", flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    private void initView() {

        int position = 0;
        if (flag == 0) {

            position = 0;
        } else {

            position = 2;
        }

        //会员种类

        List<VipKind> vipKindList = new ArrayList<>();
        //本应用会员
        VipKind thisKind = new VipKind(R.mipmap.icon_this_vip, null, getResources().getString(R.string.vip_desc1), 1);
        thisKind.setVip_intro(R.string.vip_intro1_new);
        VipKind.Kind kind1 = new VipKind.Kind(getString(R.string.this_vip_time1), 25, 1);
        VipKind.Kind kind2 = new VipKind.Kind(getString(R.string.this_vip_time2), 69, 6);
        VipKind.Kind kind3 = new VipKind.Kind(getString(R.string.this_vip_time3), 99, 12);
        VipKind.Kind kind4 = new VipKind.Kind(getString(R.string.this_vip_time4), 199, 36);
        thisKind.getKindList().add(kind1);
        thisKind.getKindList().add(kind2);
        thisKind.getKindList().add(kind3);
        thisKind.getKindList().add(kind4);


        //全站会员
        VipKind allKind = new VipKind(R.mipmap.icon_all_vip, getResources().getString(R.string.vip_desc2), getResources().getString(R.string.vip_desc_mic1), 2);
        allKind.setVip_intro(R.string.vip_intro2);
        VipKind.Kind allKind1 = new VipKind.Kind(getString(R.string.all_vip_time1), 50, 1);
        VipKind.Kind allKind2 = new VipKind.Kind(getString(R.string.all_vip_time2), 198, 6);
        VipKind.Kind allKind3 = new VipKind.Kind(getString(R.string.all_vip_time3), 298, 12);
        VipKind.Kind allKind4 = new VipKind.Kind(getString(R.string.all_vip_time4), 588, 36);
        allKind.getKindList().add(allKind1);
        allKind.getKindList().add(allKind2);
        allKind.getKindList().add(allKind3);
        allKind.getKindList().add(allKind4);


        //黄金会员
        VipKind goldKind = new VipKind(R.mipmap.icon_gold_vip, getResources().getString(R.string.vip_desc3), getResources().getString(R.string.vip_desc_mic2), 3);
        goldKind.setVip_intro(R.string.vip_intro3);
        VipKind.Kind gKind1 = new VipKind.Kind(getString(R.string.gold_vip_time1), 98, 1);
        VipKind.Kind gKind2 = new VipKind.Kind(getString(R.string.gold_vip_time2), 288, 3);
        VipKind.Kind gKind3 = new VipKind.Kind(getString(R.string.gold_vip_time3), 518, 6);
        VipKind.Kind gKind4 = new VipKind.Kind(getString(R.string.gold_vip_time4), 998, 12);

        goldKind.getKindList().add(gKind1);
        goldKind.getKindList().add(gKind2);
        goldKind.getKindList().add(gKind3);
        goldKind.getKindList().add(gKind4);


        vipKindList.add(thisKind);
        vipKindList.add(allKind);
        vipKindList.add(goldKind);

        activityVipBinding.vipRvVip.setLayoutManager(new GridLayoutManager(getApplicationContext(), vipKindList.size()) {

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        vipAdapter = new VipAdapter(R.layout.item_vip, vipKindList);
        activityVipBinding.vipRvVip.setAdapter(vipAdapter);
        vipAdapter.setPosition(0);


        vipAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                vipAdapter.setPosition(i);
                VipKind vipKind = vipAdapter.getItem(vipAdapter.getPosition());
                //说明
                activityVipBinding.vipIntro.setText(vipKind.getVip_intro());
                //价格
                vipKindAdapter.setNewData(vipKind.getKindList());
                vipKindAdapter.setPostion(0);
            }
        });

        //会员价格
        vipKindAdapter = new VipKindAdapter(R.layout.item_vip_kind, vipAdapter.getItem(position).getKindList());
        activityVipBinding.vipRvKind.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activityVipBinding.vipRvKind.setAdapter(vipKindAdapter);
        activityVipBinding.vipRvKind.setNestedScrollingEnabled(false);
        vipKindAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                vipKindAdapter.setPostion(i);
            }
        });

        vipAdapter.getOnItemClickListener().onItemClick(vipAdapter, null, position);
    }


    private void bindView() {

        activityVipBinding.vipHead.setOnClickListener(this);
        activityVipBinding.vipSubmit.setOnClickListener(this);
        activityVipBinding.back.setOnClickListener(this);
        activityVipBinding.vipTvIyubi.setOnClickListener(this);


        activityVipBinding.vipBuyCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getQQGroup("riyu");
            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.back) {
            onBackPressed();
        } else if (id == R.id.vip_tv_iyubi) {
            //购买爱语币
            if (Constant.userinfo != null) {
                Intent intent = new Intent(this, BuyIyuBiActivity.class);
                intent.putExtra("uname", Constant.userinfo.getNickname());
                startActivity(intent);
                VipActivity.this.finish();
            } else {
                showDialog();
            }
        } else if (id == R.id.vip_submit) {//立即开通

            if (Constant.userinfo != null) {

                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("uname", Constant.userinfo.getUsername());
                intent.putExtra("desc", vipKindAdapter.getItem(vipKindAdapter.getPostion()).getText());
                intent.putExtra("month", vipKindAdapter.getItem(vipKindAdapter.getPostion()).getMonth());
                intent.putExtra("cate", getCate());
                intent.putExtra("price", String.valueOf(vipKindAdapter.getItem(vipKindAdapter.getPostion()).getPrice()));
                startActivity(intent);
                // VipActivity.this.finish();
            } else {
                showDialog();
                // Toast.makeText(this, "请先登录!", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.vip_head) {
            if (Constant.userinfo == null) {
                showDialog();
            }
        }
    }


    private String getCate() {
        String cate = "本应用会员";

        int mCurCate = vipAdapter.getItem(vipAdapter.getPosition()).getVipFlag();
        if (mCurCate == 1) {
            cate = "本应用会员";
        } else if (mCurCate == 2) {
            cate = "全站会员";
        } else if (mCurCate == 3) {
            cate = "黄金会员";
        }
        return cate;
    }

    /**
     * 登录弹窗
     */
    public void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_diy);
            window.setGravity(Gravity.CENTER);

            TextView tvContent = window.findViewById(R.id.dialog_content);
            tvContent.setText("您还未登录，是否跳转登录界面？");

            TextView tvCancel = window.findViewById(R.id.dialog_cancel);
            TextView tvAgree = window.findViewById(R.id.dialog_agree);
            tvAgree.setText("跳转登录");

            tvCancel.setOnClickListener(v -> alertDialog.cancel());

            tvAgree.setOnClickListener(v -> {
                alertDialog.cancel();
                // 跳转登录界面
                Intent intent = new Intent(VipActivity.this, WxLoginActivity.class);
                startActivity(intent);
//                EventBus.getDefault().post(new VerifyEventBus());
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    /**
     * 更新最新的名称、头像、会员信息
     */
    private void updateUi() {

        if (Constant.userinfo != null) {
            activityVipBinding.vipNickname.setText(Constant.userinfo.getUsername());
            activityVipBinding.biCount.setText("爱语币余额: " + Constant.userinfo.getAmount());
            // 用户头像
            if (Constant.userinfo.getImgSrc().startsWith("http://")) {

                Glide.with(this).load(Constant.userinfo.getImgSrc()).into(activityVipBinding.vipHead);
            } else {

                Glide.with(this).load(Constant.URL_STATIC1 + "/uc_server/" + Constant.userinfo.getImgSrc()).into(activityVipBinding.vipHead);
            }
            // 设置会员时间
            String vt = Constant.userinfo.getExpireTime() + "";
            if (vt.length() == 10) {//处理时间戳位数不一致的问题
                vt = vt + "000";
            }
            if (System.currentTimeMillis() > Long.parseLong(vt)) {

                activityVipBinding.vipIvKing.setVisibility(View.GONE);
                activityVipBinding.vipTime.setText("普通用户");
            } else {

                activityVipBinding.vipIvKing.setVisibility(View.VISIBLE);
                activityVipBinding.vipTime.setText("会员到期: " + TimeUtils.timeStamp2Date(vt));
            }
        } else {
            activityVipBinding.vipNickname.setText("未登录");
            activityVipBinding.vipHead.setImageResource(R.drawable.default_avatar);
            activityVipBinding.vipTime.setText("普通用户");
            activityVipBinding.biCount.setVisibility(View.GONE);
        }
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMoreInfoComplete(MoreInfoBean moreInfoBean) {


        Constant.userinfo.setExpireTime(moreInfoBean.getExpireTime());
        Constant.userinfo.setImgSrc(moreInfoBean.getMiddleUrl());

        SharedPreferences sp = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        sp.edit().putString(Constant.SP_KEY_USER_INFO, new Gson().toJson(Constant.userinfo)).apply();


        updateUi();
    }

    @Override
    public void showQQDialog(JpQQBean jpQQBean, JpQQBean2 jpQQBean2) {

        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), activityVipBinding.vipBuyCustomer);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home_qq, popupMenu.getMenu());

        //popupMenu.getMenu().getItem(0).setTitle(String.format("日语用户群: %s", BrandUtil.getQQGroupNumber(mContext)));
        popupMenu.getMenu().getItem(0).setTitle(String.format("日语用户群: %s", jpQQBean2.getQq()));
        popupMenu.getMenu().getItem(1).setTitle("内容QQ: " + jpQQBean.getData().get(0).getEditor());
        popupMenu.getMenu().getItem(2).setTitle("技术QQ: " + jpQQBean.getData().get(0).getTechnician());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.qq_group:
//                        String groupKey = BrandUtil.getQQGroupKey(mContext);
                        QQUtil.startQQGroup(VipActivity.this, jpQQBean2.getKey());
                        return true;
                    case R.id.qq_server:
                        QQUtil.startQQ(VipActivity.this, jpQQBean.getData().get(0).getEditor() + "");
                        return true;
                    case R.id.qq_tech:
                        QQUtil.startQQ(VipActivity.this, jpQQBean.getData().get(0).getTechnician() + "");
                        return true;
                    case R.id.qq_customer:

                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008881905")));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

}