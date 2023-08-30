package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request


class MainActivity : AppCompatActivity() {

    private lateinit var etFrom: EditText
    private lateinit var txtResult: TextView
    private lateinit var spnOne: Spinner
    private lateinit var spnTwo: Spinner
    private lateinit var btnConversionButton: Button
    private var baseCurrency = "EUR"
    private var targetCurrency = "INR"
    private var conversionRate = 0f
    private val key = "72b720271053d40a94007578f352ec54"
    private val apiUrl = "http://api.exchangeratesapi.io/v1/latest?access_key="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etFrom = findViewById(R.id.input)
        txtResult = findViewById(R.id.result)
        spnOne = findViewById(R.id.from)
        spnTwo = findViewById(R.id.to)
        btnConversionButton = findViewById(R.id.button)


        val currencyOptions = resources.getStringArray(R.array.currencies)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnOne.adapter = adapter


        val currencyOptionsTwo = resources.getStringArray(R.array.currencies)
        val adapterTwo = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyOptionsTwo)
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTwo.adapter = adapterTwo

        spnOne.setSelection(adapter.getPosition(baseCurrency))
        spnTwo.setSelection(adapterTwo.getPosition(targetCurrency))


        btnConversionButton.setOnClickListener {
            baseCurrency = spnOne.selectedItem.toString()
            targetCurrency = spnTwo.selectedItem.toString()
            convertCurrency()
        }




    }

    private fun convertCurrency() {
        val requestUrl = "$apiUrl$key"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, requestUrl, null,
            { response ->
                val rates = response.getJSONObject("rates")
                if (rates.has(targetCurrency)) {
                    conversionRate = rates.getDouble(targetCurrency).toFloat()
                    val inputValue = etFrom.text.toString().toFloat()
                    val convertedValue = inputValue * conversionRate
                    txtResult.text = convertedValue.toString()
                }
            },
            { error ->
                Toast.makeText(this, "Currency conversion failed", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}