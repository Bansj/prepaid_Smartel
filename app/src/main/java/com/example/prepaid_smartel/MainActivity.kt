package com.example.prepaid_smartel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var phoneNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneNumberEditText = findViewById(R.id.editTxt_phoneNums)
        val inquiryButton = findViewById<Button>(R.id.btn_search)

        inquiryButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()
            val intent = Intent(this, PrepaidInfoActivity::class.java)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)
        }
    }
}
