package com.example.prepaid_smartel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    private lateinit var phoneInput: EditText
    private lateinit var searchButton: Button
    private lateinit var loadingSpinner: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to UI elements
        phoneInput = findViewById(R.id.btn_editText)
        phoneInput.gravity = Gravity.CENTER


        searchButton = findViewById(R.id.btn_search)
        loadingSpinner = findViewById(R.id.loading_spinner)

        // Set loading spinner color to orange
        loadingSpinner.indeterminateDrawable.setColorFilter(ContextCompat.getColor(
            this, R.color.orange),
            android.graphics.PorterDuff.Mode.MULTIPLY)

        // Set up click listener for search button
        searchButton.setOnClickListener {
            val phoneNumber = phoneInput.text.toString()
            fetchPrepaidInfo(phoneNumber)
        }
    }


    private fun fetchPrepaidInfo(phoneNumber: String) {
        val url = "http://61.41.9.34/lg_RealTime/api_prepaid_info.php"

        // Create JSON object with phone number parameter
        val jsonObject = JSONObject()
        jsonObject.put("hp_no", phoneNumber)

        // Show loading spinner
        loadingSpinner.visibility = View.VISIBLE

        // Make request to API with Volley library
        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            { response ->
                // On successful response, log each item and start PrepaidInfoActivity
                Log.d("PrepaidInfo", "Response: $response")
                val carrier = if (!response.isNull("carrier")) response.getString("carrier") else ""
                Log.d("PrepaidInfo", "Carrier: $carrier")
                val rateNm = if (!response.isNull("rateNm")) response.getString("rateNm") else ""
                Log.d("PrepaidInfo", "Rate name: $rateNm")
                val rateAmt = if (!response.isNull("rate_amt")) response.getString("rate_amt") else ""
                Log.d("PrepaidInfo", "Rate amount: $rateAmt")
                val remain = if (!response.isNull("remain")) response.getString("remain") else ""
                Log.d("PrepaidInfo", "Remain: $remain")
                val bank = if (!response.isNull("bank")) response.getString("bank") else ""
                Log.d("PrepaidInfo", "Bank: $bank")
                val bankAcnt = if (!response.isNull("bank_acnt")) response.getString("bank_acnt") else ""
                Log.d("PrepaidInfo", "Bank account: $bankAcnt")

                val intent = Intent(this, PrepaidInfoActivity::class.java)
                intent.putExtra("carrier", carrier ?: "")
                intent.putExtra("rateNm", rateNm ?: "")
                intent.putExtra("rateAmt", rateAmt ?: "")
                intent.putExtra("remain", remain ?: "")
                intent.putExtra("bank", bank ?: "")
                intent.putExtra("bankAcnt", bankAcnt ?: "")
                startActivity(intent)

                // Hide loading spinner
                loadingSpinner.visibility = View.GONE

            },

            { error ->
                // Show loading spinner
                loadingSpinner.visibility = View.GONE
                // On error, show error message
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("PrepaidInfo", "Error: ${error.message}")
            }
        )

        // Add request to Volley queue
        Volley.newRequestQueue(this).add(request)
    }
}
