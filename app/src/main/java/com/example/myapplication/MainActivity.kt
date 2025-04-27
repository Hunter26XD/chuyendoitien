package com.example.myapplication

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
private lateinit var editTextFrom: EditText
private lateinit var editTextTo: EditText
private lateinit var spinnerFrom: Spinner
private lateinit var spinnerTo: Spinner

private var isFromEditing = true

// Tỷ giá giả định (1 đơn vị sang VND)
private val rates = mapOf(
    "VND" to 1.0,
    "USD" to 24000.0,
    "EUR" to 26000.0,
    "GBP" to 30000.0,
    "JPY" to 170.0,
    "AUD" to 16000.0,
    "CAD" to 17500.0,
    "CHF" to 27500.0,
    "CNY" to 3500.0,
    "KRW" to 18.0
)

private val currencies = rates.keys.toList()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextFrom = findViewById(R.id.editTextFrom)
        editTextTo = findViewById(R.id.editTextTo)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)

        // Adapter cho spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.setSelection(1) // Mặc định USD
        spinnerTo.setSelection(0)   // Mặc định VND

        // Khi nhập vào editTextFrom
        editTextFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isFromEditing) convertCurrency(true)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Khi bấm vào EditTextFrom
        editTextFrom.setOnClickListener {
            isFromEditing = true
            editTextFrom.isFocusableInTouchMode = true
            editTextTo.isFocusable = false
        }

        // Khi nhập vào editTextTo (nếu người dùng muốn nhập ngược lại)
        editTextTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isFromEditing) convertCurrency(false)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextTo.setOnClickListener {
            isFromEditing = false
            editTextTo.isFocusableInTouchMode = true
            editTextFrom.isFocusable = false
        }

        // Nếu thay đổi loại tiền tệ -> chuyển đổi lại
        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                convertCurrency(isFromEditing)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                convertCurrency(isFromEditing)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    private fun convertCurrency(fromEditText: Boolean) {
        try {
            val fromCurrency = spinnerFrom.selectedItem.toString()
            val toCurrency = spinnerTo.selectedItem.toString()

            val fromRate = rates[fromCurrency] ?: 1.0
            val toRate = rates[toCurrency] ?: 1.0

            if (fromEditText) {
                val amount = editTextFrom.text.toString().toDoubleOrNull() ?: 0.0
                val result = amount * fromRate / toRate
                editTextTo.setText(String.format("%.2f", result))
            } else {
                val amount = editTextTo.text.toString().toDoubleOrNull() ?: 0.0
                val result = amount * toRate / fromRate
                editTextFrom.setText(String.format("%.2f", result))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}