package com.example.prepaid_smartel

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PrepaidInfoActivity : AppCompatActivity() {
    private lateinit var numberTextView: TextView
    private lateinit var carrierTextView: TextView
    private lateinit var ratePlanTextView: TextView
    private lateinit var rateAmountTextView: TextView
    private lateinit var remainTextView: TextView
    private lateinit var bankTextView: TextView
    private lateinit var bankAccountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepaid_info)

        numberTextView = findViewById(R.id.numberTextView)
        carrierTextView = findViewById(R.id.carrierTextView)
        ratePlanTextView = findViewById(R.id.ratePlanTextView)
        rateAmountTextView = findViewById(R.id.rateAmountTextView)
        remainTextView = findViewById(R.id.remainTextView)
        bankTextView = findViewById(R.id.bankTextView)
        bankAccountTextView = findViewById(R.id.bankAccountTextView)

        val number = intent.getStringExtra("number")

        // Call AsyncTask to retrieve prepaid information using the API
        val prepaidInfoTask = PrepaidInfoTask()
        prepaidInfoTask.execute(number)
    }

    inner class PrepaidInfoTask : AsyncTask<String, Void, JSONObject>() {
        override fun doInBackground(vararg params: String?): JSONObject? {
            val number = params[0]
            val url = "http://61.41.9.34/lg_RealTime/api_prepaid_info.php?hp_no=$number"

            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()

            bufferedReader.use {
                var line = it.readLine()
                while (line != null) {
                    response.append(line)
                    line = it.readLine()
                }
            }

            return JSONObject(response.toString())
        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            if (result != null) {
                numberTextView.text = result.getString("hp_no")
                carrierTextView.text = result.getString("carrier")
                ratePlanTextView.text = result.getString("rateNm")
                rateAmountTextView.text = result.getString("rate_amt")
                remainTextView.text = result.getString("remain")
                bankTextView.text = result.getString("bank")
                bankAccountTextView.text = result.getString("bank_acnt")
            }
        }
    }
}

