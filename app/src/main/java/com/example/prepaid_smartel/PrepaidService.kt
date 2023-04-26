package com.example.prepaid_smartel

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PrepaidService {
    @FormUrlEncoded
    @POST("myapp_vacct_acnt.php")
    fun getAccountInfo(
        @Field("hp_no") phoneNumber: String
    ): Call<AccountInfoResponse>


    @FormUrlEncoded
    @POST("myapp_pps_remain.php")
    fun getBalanceInfo(
        @Field("hp_no") phoneNumber: String
    ): Call<BalanceInfoResponse>
}