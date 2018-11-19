package com.itdlc.android.library.http.api;

import android.support.annotation.NonNull;

import com.itdlc.android.library.base.BaseResp;
import com.itdlc.android.library.http.entity.AliPayResult;
import com.itdlc.android.library.http.entity.WechatPayInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 应用接口清单
 */

public interface LibApiService {

    /**
     * @param outTradeNo 流水号
     * @param payType    给0
     */
    @POST("front/payOrders")
    Observable<BaseResp> payByBalance(@NonNull @Query("orderId") String outTradeNo
            , @Query("payType") int payType);

    /**
     * 支付宝充值
     */
    @POST("front/insertRecharge")
    Observable<AliPayResult> aliPayRecharge(@NonNull @Query("rechargeOptionId") String rechargeOptionId
            , @Query("payType") int payType
            , @NonNull @Query("money") double money);

    /**
     * 支付宝支付
     */
    @POST("front/payOrders")
    Observable<AliPayResult> getAliPayInfo(@NonNull @Query("ordersId") String outTradeNo, @Query("addId") String addId, @Query("billInfoId") String billInfoId
            , @Query("payType") int payType);

    /**
     * @param payType 给0
     */
    @POST("front/payOrders")
    Observable<WechatPayInfo> getWechatPayInfo(@NonNull @Query("ordersId") String outTradeNo, @Query("addId") String addId, @Query("billInfoId") String billInfoId
            , @Query("payType") int payType);

    /**
     * 微信充值
     */
    @POST("front/insertRecharge")
    Observable<WechatPayInfo> weChatRecharge(@NonNull @Query("rechargeOptionId") String rechargeOptionId
            , @Query("payType") int payType
            , @NonNull @Query("money") double money);

    //新增叫修服务
    @POST
    @Multipart
    Observable<WechatPayInfo> weChatService(@Url String url, @Part List<MultipartBody.Part> data);

    @POST
    @Multipart
    Observable<AliPayResult> aliService(@Url String url, @Part List<MultipartBody.Part> data);

    @POST
    @Multipart
    Observable<BaseResp> balanceService(@Url String url, @Part List<MultipartBody.Part> data);

}