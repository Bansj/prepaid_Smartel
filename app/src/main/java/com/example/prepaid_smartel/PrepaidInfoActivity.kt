package com.example.prepaid_smartel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PrepaidInfoActivity : AppCompatActivity() {

    private lateinit var carrierText: TextView
    private lateinit var rateNameText: TextView
    private lateinit var rateAmountText: TextView
    private lateinit var remainingText: TextView
    private lateinit var bankText: TextView
    private lateinit var bankAccountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepaid_info)

        // Get references to UI elements
        carrierText = findViewById(R.id.result_carrier)
        rateNameText = findViewById(R.id.result_ratePlan)
        rateAmountText = findViewById(R.id.result_rateAmount)
        remainingText = findViewById(R.id.result_remain)

        bankAccountText = findViewById(R.id.result_bankAccount)

        // Get data from intent and display in UI
        val carrier = intent.getStringExtra("carrier")
        carrierText.text = "$carrier"
        val rateNm = intent.getStringExtra("rateNm")
        rateNameText.text = "$rateNm"
        val rateAmt = intent.getStringExtra("rateAmt")
        rateAmountText.text = "$rateAmt 원"
        val remain = intent.getStringExtra("remain")
        remainingText.text = "$remain"
        val bank = intent.getStringExtra("bank")

        val bankAcnt = intent.getStringExtra("bankAcnt")
        bankAccountText.text = "$bank $bankAcnt"
    }

}

