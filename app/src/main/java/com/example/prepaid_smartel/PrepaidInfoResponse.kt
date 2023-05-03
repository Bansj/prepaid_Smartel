package com.example.prepaid_smartel

data class PrepaidInfoResponse(

    val hp_no: String,
    val carrier: String,
    val rateNm: String,
    val rate_amt: String,
    val remain: String,
    val bank: String,
    val bank_acnt: String

)
