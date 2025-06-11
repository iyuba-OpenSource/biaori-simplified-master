package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.model.bean.vip.JpQQBean2;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MServer {


    /**
     * 获取QQ群
     *
     * @param type
     * @return
     */
    @GET("/m_login/getQQGroup.jsp")
    Observable<JpQQBean2> getQQGroup(@Query("type") String type);
}
