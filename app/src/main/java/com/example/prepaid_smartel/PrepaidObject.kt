package com.example.prepaid_smartel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PrepaidObject {

    object PrepaidObject {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://61.41.9.34/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val api = retrofit.create(PrepaidInfoApi::class.java)

        suspend fun getPrepaidInfo(hp_no: String): PrepaidInfoResponse {
            val body = mapOf("hp_no" to hp_no)
            return api.getPrepaidInfo(body)
        }
    }

}