package com.example.prepaid_smartel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class PrepaidInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepaid_info)

        // 로그아웃 버튼을 누르면 정보 조회 액티비티에서 메인 액티비티 창으로 화면전환 하는 클릭 이벤트
        val btnLogOut = findViewById<ImageButton>(R.id.btn_quit)
        btnLogOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}