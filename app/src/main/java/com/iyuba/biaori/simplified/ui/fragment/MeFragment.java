package com.iyuba.biaori.simplified.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.FragmentMeBinding;
import com.iyuba.biaori.simplified.entity.Contact;
import com.iyuba.biaori.simplified.model.bean.login.LogoffBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean;
import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;
import com.iyuba.biaori.simplified.presenter.me.MePresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.dubbing.MyDubbingActivity;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.ui.me.IntegralShopActivity;
import com.iyuba.biaori.simplified.ui.me.LessonCollectActivity;
import com.iyuba.biaori.simplified.ui.me.MyDownloadActivity;
import com.iyuba.biaori.simplified.ui.me.MyWalletActivity;
import com.iyuba.biaori.simplified.ui.me.RankingListActivity;
import com.iyuba.biaori.simplified.ui.me.SignActivity;
import com.iyuba.biaori.simplified.ui.me.WordCollectActivity;
import com.iyuba.biaori.simplified.ui.vip.VipActivity;
import com.iyuba.biaori.simplified.ui.welcome.MyWebActivity;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.util.LoginUtil;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.util.QQUtil;
import com.iyuba.biaori.simplified.util.popup.ContactPopup;
import com.iyuba.biaori.simplified.util.popup.LoadingPopup;
import com.iyuba.biaori.simplified.util.popup.MePriPopup;
import com.iyuba.biaori.simplified.view.me.MeContract;
import com.iyuba.imooclib.ui.record.PurchaseRecordActivity;
import com.iyuba.module.favor.ui.BasicFavorActivity;
import com.iyuba.module.toolbox.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.PersonalType;
import personal.iyuba.personalhomelibrary.ui.groupChat.GroupChatManageActivity;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
import personal.iyuba.personalhomelibrary.ui.message.MessageActivity;
import personal.iyuba.personalhomelibrary.ui.my.MySpeechActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;

/**
 * 我的fragment
 */
public class MeFragment extends BaseFragment<MeContract.MeView, MeContract.MePresenter>
        implements MeContract.MeView {

    private FragmentMeBinding fragmentMeBinding;

    private MePriPopup mePriPopup;

    /**
     * 注销的弹窗
     */
    private ProgressDialog progressDialog;

    private LoadingPopup loadingPopup;

    /**
     * 联系我们弹窗
     */
    private ContactPopup contactPopup;

    public MeFragment() {
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentMeBinding = FragmentMeBinding.inflate(inflater, container, false);
        return fragmentMeBinding.getRoot();
    }

    @Override
    protected MeContract.MePresenter initPresenter() {
        return new MePresenter();
    }

    private void initOperation() {

        fragmentMeBinding.meLlBuylog.setOnClickListener(v -> {

            if (Constant.userinfo == null) {

                startActivity(new Intent(getActivity(), WxLoginActivity.class));
            } else {

                startActivity(PurchaseRecordActivity.buildIntent(requireActivity()));
            }
        });

        fragmentMeBinding.meLlWallet.setOnClickListener(v -> {

            if (Constant.userinfo == null) {

                startActivity(new Intent(getActivity(), WxLoginActivity.class));
            } else {

                startActivity(new Intent(getActivity(), MyWalletActivity.class));
            }
        });
        fragmentMeBinding.meTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

//                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                }
            }
        });
        //头像
        fragmentMeBinding.meIvPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    //跳转到个人中心界面
                    startActivity(PersonalHomeActivity.buildIntent(getContext(), Constant.userinfo.getUid(), Constant.userinfo.getUsername(), 0));
                }
            }
        });
        fragmentMeBinding.meTvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUtil.logout();
                updateUserUI();
            }
        });
        fragmentMeBinding.meTvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPrivacyPopup();
            }
        });
        fragmentMeBinding.meLlVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VipActivity.startActivity(getActivity(), 0);
            }
        });
        //我的收藏
        fragmentMeBinding.meLlCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    startActivity(new Intent(getActivity(), LessonCollectActivity.class));
                }
            }
        });
        //注销账号
        fragmentMeBinding.meTvLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showWarning();
            }
        });
        //单词本
        fragmentMeBinding.meLlCollectionWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    toast("请登录");
                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), WordCollectActivity.class));
            }
        });
        fragmentMeBinding.meLlDubbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    toast("请登录");
                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    startActivity(new Intent(getActivity(), MyDubbingActivity.class));
                }
            }
        });
        //视频收藏
        fragmentMeBinding.meLlHeadlineCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    startActivity(BasicFavorActivity.buildIntent(getContext()));
                }
            }
        });
        //积分商城
        fragmentMeBinding.meLlPointsMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    String username = Constant.userinfo.getUsername();
                    String url = Constant.URL_M + "/mall/index.jsp?"
                            + "&uid=" + Constant.userinfo.getUid()
                            + "&sign=" + MD5.getMD5ofStr("iyuba" + Constant.userinfo.getUid() + "camstory")
                            + "&username=" + username
                            + "&platform=android&appid="
                            + Constant.APPID;
                    IntegralShopActivity.startActivity(getActivity(), url);
                }
            }
        });
        fragmentMeBinding.meLlKyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    Toast.makeText(MyApplication.getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    startActivity(MySpeechActivity.buildIntent(getContext()));
                }
            }
        });
        //我的音频下载
        fragmentMeBinding.meLlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MyDownloadActivity.class));
            }
        });
        //同步数据
        fragmentMeBinding.meLlSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo != null) {

                    String sign = MD5.getMD5ofStr(Constant.userinfo.getUid() + DateUtil.getCurDate());
                    presenter.getStudyRecordByTestMode("json", Constant.userinfo.getUid(), 1, 10, 1, sign, "biaori");


//                    presenter.getTestRecord(Constant.userinfo.getUid() + "", "jp3");

/*                    String signStr = Constant.userinfo.getUid() + "jp3" + 2 + "W" + Constant.APPID + DateUtil.getCurDate();
                    String sign = MD5Util.MD5(signStr);
                    presenter.getExamDetail(Constant.APPID, Constant.userinfo.getUid() + "", "jp3", "W",
                            2, sign, "json");*/
                }
            }
        });

        //排行榜
        fragmentMeBinding.meLlRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), RankingListActivity.class));
            }
        });
        //学习报告
        fragmentMeBinding.meLlStudyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    toast("请登录");
                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    return;
                }

                String[] types = new String[]{
                        SummaryType.LISTEN,
                        SummaryType.EVALUATE,
                        SummaryType.WORD,
//                        SummaryType.TEST,
                        //SummaryType.MOOC,
//                    SummaryType.READ
                };
                startActivity(SummaryActivity.buildIntent(requireContext(), "all", types, 0, "D", "Japan"));//10 PersonalType.NEWS
            }
        });
        //打卡签到
        fragmentMeBinding.meLlSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                } else {

                    startActivity(new Intent(requireActivity(), SignActivity.class));
                }
            }
        });
        //联系我们
        fragmentMeBinding.meLlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getQQGroup("riyu");
            }
        });
        //消息中心
        fragmentMeBinding.meLlMsgCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    toast("请登录");
                } else {

                    startActivity(new Intent(getContext(), MessageActivity.class));
                }
            }
        });
        //官方群
        fragmentMeBinding.meLlGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo != null) {

                    GroupChatManageActivity.start(requireActivity(), 10104, "日语官方群", true);
                } else {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                }
            }
        });
    }


    private void initPrivacyPopup() {

        if (mePriPopup == null) {

            List<String> stringList = new ArrayList<>();
            stringList.add("用户协议");
            stringList.add("隐私政策");
            mePriPopup = new MePriPopup(getContext());
            mePriPopup.initOperation(stringList);
            mePriPopup.setCallback(new MePriPopup.Callback() {
                @Override
                public void getString(String s) {

                    if (s.equals("用户协议")) {

                        MyWebActivity.startActivity(getActivity(), Constant.URL_PROTOCOLUSE, "用户协议");
                        mePriPopup.dismiss();

                    } else {

                        MyWebActivity.startActivity(getActivity(), Constant.URL_PROTOCOLPRI, "隐私协议");
                        mePriPopup.dismiss();
                    }
                }
            });
        }
        mePriPopup.setPopupGravity(Gravity.BOTTOM);
        mePriPopup.showPopupWindow();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUserUI();
    }

    private void updateUserUI() {


        if (Constant.userinfo == null) {

            fragmentMeBinding.meTvExit.setVisibility(View.GONE);
            fragmentMeBinding.meTvLogin.setText("登录");
            Glide.with(getContext()).load(R.drawable.default_avatar).into(fragmentMeBinding.meIvPortrait);
            fragmentMeBinding.meTvLogoff.setVisibility(View.GONE);
        } else {

            fragmentMeBinding.meTvExit.setVisibility(View.VISIBLE);
            fragmentMeBinding.meTvLogin.setText(Constant.userinfo.getUsername());
            fragmentMeBinding.meTvLogoff.setVisibility(View.VISIBLE);
            if (Constant.userinfo.getImgSrc().startsWith("http://")) {

                Glide.with(getContext()).load(Constant.userinfo.getImgSrc()).into(fragmentMeBinding.meIvPortrait);
            } else {

                Glide.with(getContext()).load(Constant.URL_STATIC1 + "/uc_server/" + Constant.userinfo.getImgSrc()).into(fragmentMeBinding.meIvPortrait);
            }
        }
    }

    /**
     * 是否要注销的弹窗
     */
    private void showWarning() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        final View diaView = getLayoutInflater().inflate(R.layout.dialog_logoff, null);
        builder.setView(diaView);
        builder.setPositiveButton("继续注销", (dialog, which) -> PressPwd());
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    /**
     * 注销  输入密码的弹窗
     */
    private void PressPwd() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("输入密码注销账号");
        final View diaView2 = getLayoutInflater().inflate(R.layout.dialog_logoff2, null);
        final EditText et = diaView2.findViewById(R.id.logout_pwd);
        builder.setView(diaView2);
        builder.setPositiveButton("确认注销", (dialog, which) -> {

            String username = Constant.userinfo.getUsername();
            String pressPwd = et.getText().toString();
            String signStr = MD5Util.MD5("11005" + username + MD5Util.MD5(pressPwd) + "iyubaV2");
            progressDialog = ProgressDialog.show(getActivity(), "注销中...", "", true);

            //请求注销
            try {
                presenter.logoff(11005, URLEncoder.encode(username, "utf-8"), MD5Util.MD5(pressPwd), "json", signStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(requireActivity());
        }
        loadingPopup.setContent(msg);
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
    public void logoffComplete(LogoffBean logoffBean) {

        LoginUtil.logout();
        updateUserUI();
    }

    @Override
    public void hideProgressDialog() {


        if (progressDialog != null) {

            progressDialog.dismiss();
        }
    }

    @Override
    public void showQQDialog(JpQQBean jpQQBean, JpQQBean2 jpQQBean2) {


        if (contactPopup == null) {

            contactPopup = new ContactPopup(requireActivity());
        }
        contactPopup.setCallback(new ContactPopup.Callback() {
            @Override
            public void click(Contact contact, int position) {

                if (contact.getTitle().equals("日语用户群")) {

                    QQUtil.startQQGroup(MyApplication.getContext(), jpQQBean2.getKey());
                } else if (contact.getTitle().equals("内容QQ")) {
                    QQUtil.startQQ(MyApplication.getContext(), jpQQBean.getData().get(0).getEditor() + "");
                } else if (contact.getTitle().equals("技术QQ")) {
                    QQUtil.startQQ(MyApplication.getContext(), jpQQBean.getData().get(0).getTechnician() + "");
                } else if (contact.getTitle().equals("客服热线")) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getNum()));
                    startActivity(intent);
                }
            }
        });
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact("日语用户群", jpQQBean2.getQq()));
        contactList.add(new Contact("内容QQ", jpQQBean.getData().get(0).getEditor() + ""));
        contactList.add(new Contact("技术QQ", jpQQBean.getData().get(0).getTechnician() + ""));
        contactList.add(new Contact("客服热线", "4008881905"));

        contactPopup.setData(contactList);
        contactPopup.showPopupWindow();
    }
}