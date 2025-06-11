package com.iyuba.biaori.simplified.model.api;

import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.vip.AlipayOrderBean;
import com.iyuba.biaori.simplified.model.bean.vip.PayResultBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VipServer {

    /**
     * 获取支付宝订单
     *
     * @param app_id
     * @param userId
     * @param code
     * @param WIDtotal_fee
     * @param amount
     * @param product_id
     * @param WIDbody
     * @param WIDsubject
     * @return
     */
    @GET("/alipay.jsp")
    Observable<AlipayOrderBean> alipayOrder(@Query("app_id") int app_id, @Query("userId") int userId, @Query("code") String code,
                                            @Query("WIDtotal_fee") String WIDtotal_fee, @Query("amount") int amount,
                                            @Query("product_id") int product_id, @Query("WIDbody") String WIDbody,
                                            @Query("WIDsubject") String WIDsubject);



    /**
     * 更新订单状态
     *
     * @param data
     * @return
     */
    @GET("/notifyAliNew.jsp")
    Observable<PayResultBean> notifyAliNew(@Query("data") String data);
}
