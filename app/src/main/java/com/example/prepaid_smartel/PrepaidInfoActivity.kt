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
import java.text.NumberFormat
import java.util.*

class PrepaidInfoActivity : AppCompatActivity() {

    private lateinit var carrierText: TextView
    private lateinit var rateNameText: TextView
    private lateinit var rateAmountText: TextView
    private lateinit var remainingText: TextView
    private lateinit var bankText: TextView
    private lateinit var bankAccountText: TextView
    private lateinit var bankLogo: ImageView

    private lateinit var rateAmountEmptyView : TextView
    private lateinit var remainTextView2 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepaid_info)


        // Get references to UI elements
        carrierText = findViewById(R.id.result_carrier)
        rateNameText = findViewById(R.id.result_ratePlan)
        rateAmountText = findViewById(R.id.result_rateAmount)
        remainingText = findViewById(R.id.result_remain)
        bankText = findViewById(R.id.result_bank)
        bankAccountText = findViewById(R.id.result_bankAccount)

        rateAmountEmptyView = findViewById(R.id.rateAmountEmptyView)
        remainTextView2 = findViewById(R.id.remainTextView2)

        // Get data from intent and display in UI
        val carrier = intent.getStringExtra("carrier") ?: ""
        carrierText.text = "$carrier"
        val rateNm = intent.getStringExtra("rateNm") ?: ""
        rateNameText.text = rateNm?.replace("(가상)", "")

        // Check if it's a 종량요금제 (contains "pps" in rate name)
        if (rateNm?.contains("PPS") == true) {
            // Set rate amount to "X"
            rateAmountText.text = ""
            // Show rateAmountEmptyView
            rateAmountEmptyView.visibility = View.VISIBLE
            // Add "원" to remainingText
            val remain = intent.getStringExtra("remain") ?: ""
            remainingText.text = "$remain 원"
            // Show remainTextView2
            remainTextView2.visibility = View.VISIBLE
        } else {
            // Get rate amount and format it with commas and currency symbol
            val rateAmt = intent.getStringExtra("rateAmt")?.toIntOrNull() ?: 0
            // Get the appropriate currency symbol based on the device's language setting
            val currencySymbol = when (Locale.getDefault().language) {
                "ko" -> "원" // for Korean
                else -> "KR₩" // for other languages
            }
            // Format the rateAmt value with commas and the appropriate currency symbol
            val formattedRateAmt = NumberFormat.getInstance().format(rateAmt) ?: ""
            rateAmountText.text = "$formattedRateAmt $currencySymbol"
            // Hide rateAmountEmptyView
            rateAmountEmptyView.visibility = View.GONE
            // Check if remainingText has "원" and remove it if it does
            val remain = intent.getStringExtra("remain")?.replace("원", "") ?: ""
            remainingText.text = remain
            // Hide remainTextView2
            remainTextView2.visibility = View.GONE
        }




        val bank = intent.getStringExtra("bank") ?: ""
        // Replace Korean bank names with user-defined values
        val bankNames = mapOf(
            "농협" to "NongHyup",
            "국민" to "Kookmin",
            "신한" to "Shinhan",
            "우리" to "Woori",
            "기업" to "IBK"
        )
        val bankName = bankNames.getOrDefault(bank, "Unknown Bank") ?: ""
        bankText.text = "$bank $bankName"

        val bankAcnt = intent.getStringExtra("bankAcnt") ?: ""
        bankAccountText.text = "$bankAcnt"

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
            val toastMoveMessage = getString(R.string.moveToSearch)
            Toast.makeText(this, toastMoveMessage, Toast.LENGTH_SHORT).show()

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}

