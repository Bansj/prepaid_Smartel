package com.example.prepaid_smartel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class PrepaidInfoActivity : AppCompatActivity() {

    private lateinit var carrierText: TextView
    private lateinit var rateNameText: TextView
    private lateinit var rateAmountText: TextView
    private lateinit var remainingText: TextView
    private lateinit var bankText: TextView
    private lateinit var bankAccountText: TextView
    private lateinit var bankLogo: ImageView

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
        rateNameText.text = rateNm?.replace("(가상)", "")
        val rateAmt = intent.getStringExtra("rateAmt")
        rateAmountText.text = "$rateAmt ₩"
        val remain = intent.getStringExtra("remain")
        remainingText.text = "$remain"
        val bank = intent.getStringExtra("bank")

        // Replace Korean bank names with user-defined values
        val bankNames = mapOf(
            "농협" to "NongHyup",
            "국민" to "Kookmin",
            "신한" to "Shinhan",
            "우리" to "Woori",
            "기업" to "IBK"
        )
        val bankName = bankNames.getOrDefault(bank, "Unknown Bank")

        val bankAcnt = intent.getStringExtra("bankAcnt")
        bankAccountText.text = "$bank $bankName $bankAcnt"

        // Set visibility of bank icon based on bank value
        bankLogo = findViewById(R.id.nh_bank)
        if (bank == "농협") {
            bankLogo.visibility = View.VISIBLE
            bankLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nh_icon))
        } else if (bank == "국민") {
            val kbBankLogo = findViewById<ImageView>(R.id.kb_bank)
            kbBankLogo.visibility = View.VISIBLE
            kbBankLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.kb_icon))
        } else if (bank == "신한") {
            val shinhanBankLogo = findViewById<ImageView>(R.id.shinhan_bank)
            shinhanBankLogo.visibility = View.VISIBLE
            shinhanBankLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shinhan_icon))
        } else if (bank == "우리") {
            val wooriBankLogo = findViewById<ImageView>(R.id.woori_bank)
            wooriBankLogo.visibility = View.VISIBLE
            wooriBankLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.woori_icon))
        } else if (bank == "기업") {
            val ibkBankLogo = findViewById<ImageView>(R.id.ibk_bank)
            ibkBankLogo.visibility = View.VISIBLE
            ibkBankLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ibk_icon))
        }


        val copyButton = findViewById<ImageButton>(R.id.btn_copy)

        copyButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", bankAccountText.text)
            clipboard.setPrimaryClip(clip)
            val toastCopiedMessage = getString(R.string.copy_bank_acnt)
            Toast.makeText(this, toastCopiedMessage, Toast.LENGTH_SHORT).show()
        }

        // 메인메뉴로 이동하는 로그인 버튼 클릭 이벤트
        val btnQuit = findViewById<ImageButton>(R.id.btn_quit)
        btnQuit.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}

