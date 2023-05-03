package com.example.prepaid_smartel

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PrepaidInfoApi {

    @POST("lg_RealTime/api_prepaid_info.php")
    suspend fun getPrepaidInfo(@Body body: Map<String, String>): PrepaidInfoResponse

}