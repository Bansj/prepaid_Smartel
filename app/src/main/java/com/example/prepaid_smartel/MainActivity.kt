package com.example.prepaid_smartel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var phoneInput: EditText
    private lateinit var searchButton: Button
    private lateinit var loadingSpinner: ProgressBar
    lateinit var phoneNumber: String
    @RequiresApi(Build.VERSION_CODES.O)
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
        // Add text watcher to phone input field
        phoneInput.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var isDeleting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || isDeleting) {
                    return
                }

                isFormatting = true

                // Remove all non-numeric characters from the input
                val phone = s.toString().replace("[^\\d]".toRegex(), "")

                // Apply formatting to the input
                val formatted = if (phone.length >= 3) {
                    val sb = StringBuilder(phone)
                    sb.insert(3, "-")
                    if (phone.length >= 7) {
                        sb.insert(8, "-")
                    }
                    sb.toString()
                } else {
                    phone
                }

                // Set the formatted text on the input field
                phoneInput.setText(formatted)
                phoneInput.setSelection(formatted.length)

                isFormatting = false
            }
        })

        // Set up click listener for search button
        searchButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(phoneInput.windowToken, 0)
            Snackbar.make(searchButton, "선불 잔액 조회의 경우,\n" +
                    "네트워크 상황에 따라 최대 3분 이상 소요될 수 있습니다. ", Snackbar.LENGTH_LONG).show()

//            Toast.makeText(this, "선불 잔액 조회의 경우,\n" +
//                    "네트워크 상황에 따라 최대 3분 이상 소요될 수 있습니다. ", Toast.LENGTH_LONG).show()
            val phoneNumber = phoneInput.text.toString().replace("-", "")
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
        // Set timeout to 2 minutes
        request.retryPolicy = DefaultRetryPolicy(
            180 * 1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add request to Volley queue
        Volley.newRequestQueue(this).add(request)
    }
}
