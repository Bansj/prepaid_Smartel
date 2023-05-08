package com.example.prepaid_smartel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

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
        rateAmountText.text = "$rateAmt ₩"
        val remain = intent.getStringExtra("remain")
        remainingText.text = "$remain"
        val bank = intent.getStringExtra("bank")

        val bankAcnt = intent.getStringExtra("bankAcnt")
        bankAccountText.text = "$bank  $bankAcnt"

        val copyButton = findViewById<ImageButton>(R.id.btn_copy)
        val textToCopy = "This is the text to be copied"

        copyButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", bankAccountText.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "계좌번호가 복사되었습니다. ", Toast.LENGTH_SHORT).show()
        }

        // 메인메뉴로 이동하는 로그인 버튼 클릭 이벤트
        val btnQuit = findViewById<ImageButton>(R.id.btn_quit)
        btnQuit.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}

