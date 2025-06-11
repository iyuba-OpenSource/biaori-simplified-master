package com.iyuba.biaori.simplified.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import org.litepal.LitePal;

public class LoginUtil {


    public static void login(UserBean.UserinfoDTO userBean, SharedPreferences sp) {


        Constant.userinfo = userBean;
        sp.edit().putString(Constant.SP_KEY_USER_INFO, new Gson().toJson(userBean)).apply();

        //登录共通模块
        User user = new User();
        user.vipStatus = userBean.getVipStatus();
        if (userBean.getExpireTime() != 0) {
            user.vipExpireTime = Long.parseLong(userBean.getExpireTime() + "");
        }
        user.uid = userBean.getUid();
        user.credit = userBean.getCredits();
        user.name = userBean.getUsername();
        user.imgUrl = userBean.getImgSrc();
        user.email = userBean.getEmail();
        user.mobile = userBean.getMobile();
        if (!TextUtils.isEmpty(userBean.getAmount()) && !"null".equals(userBean.getAmount())) {
            user.iyubiAmount = Integer.parseInt(userBean.getAmount());
        }
        IyuUserManager.getInstance().setCurrentUser(user);
    }


    public static void logout() {


        Constant.userinfo = null;

        //更新数据库的单词，收藏状态重置

        JpWord word = new JpWord();
        word.setToDefault("collect");
        word.setToDefault("answer_status");
        word.updateAll();
        //听力进度置0
        JpLesson jpLesson = new JpLesson();
        jpLesson.setToDefault("TestNumber");
        jpLesson.updateAll();
        //句子评测重置
        Sentence sentence = new Sentence();
        sentence.setToDefault("score");
        sentence.setToDefault("recordSoundUrl");
        sentence.updateAll();


        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(Constant.SP_USER, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        IyuUserManager.getInstance().logout();
    }
}
