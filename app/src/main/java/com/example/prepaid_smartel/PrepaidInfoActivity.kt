package com.example.prepaid_smartel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
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
    //종량제 요금제일 경우에 다른 목록을 보여주기 위한 선언
    private lateinit var rateAmountEmptyView : TextView
    private lateinit var remainTextView2 : TextView

    lateinit var increaseButton: ImageButton

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

        // Get references to an array of TextViews that you want to adjust the font size of
        val myTextViews = arrayOf<TextView>(findViewById(R.id.result_carrier), findViewById(R.id.result_ratePlan), findViewById(R.id.result_rateAmount),
            findViewById(R.id.result_remain), findViewById(R.id.result_bank), findViewById(R.id.result_bankAccount), findViewById(R.id.result_bankAccount),
            findViewById(R.id.carrierTextView), findViewById(R.id.ratePlanTextView), findViewById(R.id.rateAmountTextView),
            findViewById(R.id.remainTextView), findViewById(R.id.remainTextView2), findViewById(R.id.bankAccountTextView), findViewById(R.id.my_fee))

        val increaseButton = findViewById<ImageButton>(R.id.increaseButton)
        val decreaseButton = findViewById<ImageButton>(R.id.decreaseButton)

        // Set the maximum and minimum font size limits
        val maxFontSize = 85
        val minFontSize = 50

        // Set onClickListeners for the buttons
        increaseButton.setOnClickListener {
            for (textView in myTextViews) {
                val currentSize = textView.textSize
                val newSize = currentSize + 10

                if (newSize <= maxFontSize) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
                }
            }
        }
        decreaseButton.setOnClickListener {
            for (textView in myTextViews) {
                val currentSize = textView.textSize
                val newSize = currentSize - 10

                if (newSize >= minFontSize) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
                }
            }
        }

        // Get data from intent and display in UI
        val carrier = intent.getStringExtra("carrier") ?: ""
        carrierText.text = "$carrier"
        val rateNm = intent.getStringExtra("rateNm") ?: ""
        rateNameText.text = rateNm?.replace("(가상)", "")

        val rateAmt = intent.getStringExtra("rateAmt")?.toIntOrNull() ?: 0
        val currencySymbol = when (Locale.getDefault().language) {
            "ko" -> "원" // for Korean
            else -> "KR₩" // for other languages
        }

        if (rateNm?.contains("PPS") == true) { // If it is a prepaid plan
            // Show the rateAmountEmptyView and remainTextView2
            rateAmountEmptyView.visibility = View.VISIBLE
            remainTextView2.visibility = View.VISIBLE

            // Set rateAmountEmptyView and remainingText values
            rateAmountEmptyView.text = " "
            val remain = intent.getStringExtra("remain") ?: ""
            if (Locale.getDefault().language == "ko") {
                remainingText.text = "$remain $currencySymbol"
            } else {
                remainingText.text = "$remain KR₩"
            }

        } else { // If it is a flat rate plan
            // Hide the rateAmountEmptyView and remainTextView2
            rateAmountEmptyView.visibility = View.GONE
            remainTextView2.visibility = View.GONE

            // Format the rateAmt value with commas and the appropriate currency symbol
            val formattedRateAmt = NumberFormat.getInstance().format(rateAmt) ?: ""
            if (Locale.getDefault().language == "ko") {
                rateAmountText.text = "$formattedRateAmt $currencySymbol"
            } else {
                rateAmountText.text = "$formattedRateAmt KR₩"
            }

            val remain = intent.getStringExtra("remain") ?: ""
            if (Locale.getDefault().language == "ko") {
                remainingText.text = "$remain"
            } else {
                remainingText.text = "$remain"
            }
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

        // 충전하는 사이트로 이동하는 버튼 클릭 이벤트
        val btnCharge = findViewById<Button>(R.id.btn_charge)
        btnCharge.setOnClickListener {

            val url = "https://www.smartelmall.com/sub/prepay/p_service_new_test.asp#Reload_Credit/Debit_Card"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            val toastMoveMessage = getString(R.string.toChage)
            Toast.makeText(this, toastMoveMessage, Toast.LENGTH_SHORT).show()
        }

    }
}

