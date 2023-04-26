package com.example.prepaid_smartel

data class BalanceInfoResponse(
    val hp_no: String,
    val carrier: String,
    val rateNm: String,
    val rate_amt: String,
    val remain: String
)
